package at.pooltempServer.sensor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.pooltempServer.sensor.database.SensorRepository;
import at.pooltempServer.sensor.model.Sensor;
import at.pooltempServer.temperature.model.Temperature;

@Controller
@RequestMapping("/sensor")
public class SensorController {

	@Autowired
	private SensorRepository sensorRepository;
	
	@RequestMapping(method = RequestMethod.GET, path = "/ids")
	private @ResponseBody List<String> findAllIDs() {
		return sensorRepository.findSensorIDs();
		//payload.forEach(p -> temperaturePersister.save(p));

	}
	
	@RequestMapping(method = RequestMethod.POST)
	private @ResponseBody Sensor addSensorForId(@RequestBody Sensor sensor) {
		if(!sensorRepository.exists(sensor.getId())) {
			sensorRepository.save(sensor);
			return sensor;
		}
		throw new IllegalArgumentException("The sensorID is already set!!!");
	}

	@RequestMapping(method = RequestMethod.GET)
	private @ResponseBody Iterable<Sensor> findAll() {
		return sensorRepository.findAll();
		//payload.forEach(p -> temperaturePersister.save(p));

	}
	
}
