package at.pooltempServer.temperature.controller;

import at.pooltempServer.sensor.database.SensorRepository;
import at.pooltempServer.sensor.model.Sensor;
import at.pooltempServer.temperature.database.TemperatureRepository;
import at.pooltempServer.temperature.model.Temperature;
import at.pooltempServer.temperature.model.TemperatureDTO;
import at.pooltempServer.temperature.model.TemperatureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/temperature")
public class TemperatureController {

    @Autowired
    private TemperatureRepository temperaturePersister;
    @Autowired
    private SensorRepository sensorRepository;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/new")
    private @ResponseBody TemperatureRequest addTemperature(@RequestBody TemperatureRequest temp) {
        saveTemperature(temp);
        return temp;
    }

    private void saveTemperature(@RequestBody TemperatureRequest temp) {
        if (temp.getTime() == null) {
            temp.setTime(new Date());
        }
        Sensor sensor = sensorRepository.findOne(temp.getSensorID());
        if (sensor == null) {
            sensor = new Sensor();
            sensor.setId(temp.getSensorID());
        }
        sensor.addTemperature(new Temperature(temp.getTime(), temp.getTemperature()));
        sensorRepository.save(sensor);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addAll")
    private @ResponseBody List<TemperatureRequest> addAllTemperature(@RequestBody List<TemperatureRequest> payload) {

        for (TemperatureRequest temperatureRequest : payload) {
            saveTemperature(temperatureRequest);
        }

        return payload;
    }

    @RequestMapping(method = RequestMethod.GET)
    private @ResponseBody List<TemperatureDTO> getAllTemperatures() {
        List<TemperatureDTO> temperatures = new ArrayList<>();
        sensorRepository.findAll().forEach(sensor -> {
            temperatures.addAll(sensor.getTemperatures().stream().map(temperature -> mapToDTO(temperature, sensor.getId())).collect(Collectors.toList()));
        });
        return temperatures;
    }

    @RequestMapping(method = RequestMethod.GET, params = "since")
    private @ResponseBody List<TemperatureDTO> getTemperatureSince(@RequestParam(name = "since", required = true) long since) {

        List<TemperatureDTO> temperatures = new ArrayList<>();
        sensorRepository.findAll().forEach(sensor -> {
            temperatures.addAll(sensor.getTemperatures().stream().map(temperature -> mapToDTO(temperature, sensor.getId())).collect(Collectors.toList()));
        });
        return temperatures.stream().filter(t -> t.getTime().after(new Date(since))).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, params = "sensor")
    private @ResponseBody List<TemperatureDTO> getTemperaturesForSensor(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor sensor = sensorRepository.findOne(sensorId);
        if(sensor==null){
            return new ArrayList<>();
        }

        return sensor.getTemperatures().stream().map(temp -> mapToDTO(temp, sensorId)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET,path = "/latest", params = "sensor")
    private @ResponseBody TemperatureDTO getLatestTemperaturesForSensor(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor sensor = sensorRepository.findOne(sensorId);
        if(sensor==null){
            return null;
        }

        List<TemperatureDTO> temperatureDTOS = sensor.getTemperatures().stream().map(temp -> mapToDTO(temp, sensorId)).collect(Collectors.toList());
        temperatureDTOS.sort((temp1,temp2)->temp2.getTime().compareTo(temp1.getTime()));
        return temperatureDTOS.stream().findFirst().orElse(null);
    }

    private TemperatureDTO mapToDTO(Temperature temperature, String id) {
        TemperatureDTO temperatureDTO = new TemperatureDTO();
        temperatureDTO.setTemperature(temperature.getTemperature());
        temperatureDTO.setSensorID(id);
        temperatureDTO.setTime(temperature.getTime());
        return temperatureDTO;
    }

}
