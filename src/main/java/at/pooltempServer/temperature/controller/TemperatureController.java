package at.pooltempServer.temperature.controller;

import at.pooltempServer.sensor.database.SensorRepository;
import at.pooltempServer.sensor.model.Sensor;
import at.pooltempServer.temperature.database.TemperatureRepository;
import at.pooltempServer.temperature.model.Temperature;
import at.pooltempServer.temperature.model.TemperatureDTO;
import at.pooltempServer.temperature.model.TemperatureRequest;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    private void saveTemperature(TemperatureRequest temp) {
        if (temp.getTime() == null) {
            temp.setTime(new Date());
        }
        Sensor sensor = sensorRepository.findOne(temp.getSensorID());
        if (sensor == null) {
            sensor = new Sensor();
            sensor.setId(temp.getSensorID());
        }
        Temperature temperature = new Temperature();
        temperature.setSensor(sensor);
        temperature.setTemperature(temp.getTemperature());
        temperature.setTime(temp.getTime());
        temperaturePersister.save(temperature);
    }

    private void saveTemperatures(List<TemperatureRequest> temps) {
        Map<String, List<TemperatureRequest>> tempMap = temps.stream().collect(Collectors.groupingBy(t -> t.getSensorID()));

        for (Map.Entry<String, List<TemperatureRequest>> entry : tempMap.entrySet()) {
            Sensor sensor = sensorRepository.findOne(entry.getKey());
            if (sensor == null) {
                sensor = new Sensor();
                sensor.setId(entry.getKey());
            }
            sensor.getTemperatures().addAll(entry.getValue().stream().map(t -> new Temperature(t.getTime(), t.getTemperature())).collect(Collectors.toList()));
            sensorRepository.save(sensor);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addAll")
    private @ResponseBody List<TemperatureRequest> addAllTemperature(@RequestBody List<TemperatureRequest> payload) {

        saveTemperatures(payload);
        return payload;
    }

    @RequestMapping(method = RequestMethod.GET)
    private @ResponseBody List<TemperatureDTO> getAllTemperatures() {
        List<Temperature> temperatures = Lists.newArrayList(temperaturePersister.findAll());
        return temperatures.stream().map(this::mapToDTO).collect(Collectors.toList());
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
        if (sensor == null) {
            return new ArrayList<>();
        }

        return sensor.getTemperatures().stream().map(temp -> mapToDTO(temp, sensorId)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/latest", params = "sensor")
    private @ResponseBody TemperatureDTO getLatestTemperaturesForSensor(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor sensor = sensorRepository.findOne(sensorId);
        if (sensor == null) {
            return null;
        }

        List<TemperatureDTO> temperatureDTOS = sensor.getTemperatures().stream().map(temp -> mapToDTO(temp, sensorId)).collect(Collectors.toList());
        temperatureDTOS.sort((temp1, temp2) -> temp2.getTime().compareTo(temp1.getTime()));
        return temperatureDTOS.stream().findFirst().orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/highest", params = "sensor")
    private @ResponseBody TemperatureDTO getHighestTemperatureForSensor(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor s = sensorRepository.findOne(sensorId);
        Temperature temperature = temperaturePersister.findTopBySensorEqualsOrderByTemperatureDesc(s);
        return mapToDTO(temperature);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/lowest", params = "sensor")
    private @ResponseBody TemperatureDTO getLowestTemperatureForSensor(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor s = sensorRepository.findOne(sensorId);
        Temperature temperature = temperaturePersister.findTopBySensorEqualsOrderByTemperatureAsc(s);
        return mapToDTO(temperature);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/lastDay", params = "sensor")
    private @ResponseBody double getAverageTemperatureForSensorOfYesterday(@RequestParam(name = "sensor", required = true) String sensorId) {
        Sensor s = sensorRepository.findOne(sensorId);

        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Temperature> temps = temperaturePersister.findAllBySensorEqualsAndTimeAfter(s, date);

        double tempvalue = temps.stream().map(t -> t.getTemperature()).reduce((a, b) -> a + b).get();

        return tempvalue / temps.size();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/count")
    private @ResponseBody long getCountOfEntries() {
        return temperaturePersister.countEntries();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/count", params = "sensor")
    private @ResponseBody long getCountOfEntries(@RequestParam(name = "sensor", required = true) String sensorId) {
        return temperaturePersister.countAllBySensorEquals(sensorRepository.findOne(sensorId));
    }

    private TemperatureDTO mapToDTO(Temperature temperature, String id) {
        TemperatureDTO temperatureDTO = new TemperatureDTO();
        temperatureDTO.setTemperature(temperature.getTemperature());
        temperatureDTO.setSensorID(id);
        temperatureDTO.setTime(temperature.getTime());
        return temperatureDTO;
    }

    private TemperatureDTO mapToDTO(Temperature temperature) {
        TemperatureDTO temperatureDTO = new TemperatureDTO();
        temperatureDTO.setTemperature(temperature.getTemperature());
        if (temperature.getSensor() != null) {
            temperatureDTO.setSensorID(temperature.getSensor().getId());
        }
        temperatureDTO.setTime(temperature.getTime());
        return temperatureDTO;
    }

}
