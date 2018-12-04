package at.pooltempServer.temperature.controller;

import at.pooltempServer.temperature.database.TemperatureRepository;
import at.pooltempServer.temperature.model.Temperature;
import at.pooltempServer.temperature.model.TemperatureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/new")
    private @ResponseBody Temperature addTemperature(@RequestBody Temperature temp) {
        if (temp.getTime() == null) {
            temp.setTime(new Date());
        }
        return temperaturePersister.save(temp);
    }

    @RequestMapping(method = RequestMethod.POST)
    private @ResponseBody Temperature addTemperature(@RequestParam(name = "temp", required = true) double temp,
            @RequestParam(name = "date", required = false) long dateAsLong) {
        Temperature temperature = new Temperature();
        if (dateAsLong == 0) {
            temperature.setTime(new Date());
        } else {
            temperature.setTime(new Date(dateAsLong));
        }
        temperature.setTemperature(temp);
        System.out.println("new Temperature saved: " + temperature.getTemperature());
        return temperaturePersister.save(temperature);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addAll")
    private @ResponseBody String addAllTemperature(@RequestBody List<Temperature> payload) {
        temperaturePersister.save(payload);
        //payload.forEach(p -> temperaturePersister.save(p));

        return "OK";
    }

    @RequestMapping(method = RequestMethod.GET)
    private @ResponseBody List<Temperature> getAllTemperatures() {
        ArrayList<Temperature> temperatureList = new ArrayList<>();
        temperaturePersister.findAll().forEach(temperatureList::add);
        return temperatureList;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/dto")
    private @ResponseBody List<TemperatureDTO> getAllTemperatureDTOs() {
        ArrayList<TemperatureDTO> temperatureList = new ArrayList<>();
        temperaturePersister.findAll().forEach(t -> temperatureList.add(mapToDTO(t)));
        return temperatureList;
    }

    @RequestMapping(method = RequestMethod.GET, params = "since")
    private @ResponseBody List<Temperature> getTemperatureSince(@RequestParam(name = "since", required = true) long since) {
        ArrayList<Temperature> temperatureList = new ArrayList<>();
        temperaturePersister.findAll().forEach(temperatureList::add);
        return temperatureList.stream().filter(t -> t.getTime().after(new Date(since))).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/latest", params = "sensor")
    private @ResponseBody Temperature getLatestTemperature(@RequestParam(name = "sensor", required = true) String sensor) {
        List<Temperature> temperatures = temperaturePersister.findTemperatureBySensorID(sensor);
        return temperatures.get(temperatures.size() - 1);
    }

    @RequestMapping(method = RequestMethod.GET, params = "sensor")
    private @ResponseBody List<Temperature> getTemperatureForSensor(@RequestParam(name = "sensor", required = true) String sensor) {
        return temperaturePersister.findTemperatureBySensorID(sensor);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/highest", params = "sensor")
    private @ResponseBody Temperature getHighestTemperature(@RequestParam(name = "sensor", required = true) String sensor) {
        return temperaturePersister.findFirstBySensorIDOrderByTemperatureDesc(sensor);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/lowest", params = "sensor")
    private @ResponseBody Temperature getLowestTemperature(@RequestParam(name = "sensor", required = true) String sensor) {
        return temperaturePersister.findFirstBySensorIDOrderByTemperatureDesc(sensor);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/between", params = { "startDate", "endDate" })
    private @ResponseBody List<Temperature> getTemperatureBetween(@RequestParam(name = "startDate", required = true) long startDate,
            @RequestParam(name = "endDate", required = true) long endDate) {
        return temperaturePersister.findTemperatureByTimeBetween(new Date(startDate), new Date(endDate));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/between", params = { "startDate", "endDate", "sensor" })
    private @ResponseBody List<Temperature> getTemperatureBetweenForSensor(@RequestParam(name = "startDate", required = true) long startDate,
            @RequestParam(name = "endDate", required = true) long endDate, @RequestParam(name = "sensor", required = true) String sensor) {
        return temperaturePersister.findTemperatureBySensorIDAndTimeBetween(sensor, new Date(startDate), new Date(endDate));
    }

    private TemperatureDTO mapToDTO(Temperature temperature) {
        TemperatureDTO temperatureDTO = new TemperatureDTO();
        temperatureDTO.setSensorID(temperature.getSensorID());
        temperatureDTO.setTemperature(temperature.getTemperature());
        temperatureDTO.setTime(temperature.getTime());
        return temperatureDTO;
    }

}
