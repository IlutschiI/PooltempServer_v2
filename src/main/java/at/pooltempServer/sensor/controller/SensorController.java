package at.pooltempServer.sensor.controller;

import java.util.List;

import at.pooltempServer.sensor.model.SensorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import at.pooltempServer.sensor.database.SensorRepository;
import at.pooltempServer.sensor.model.Sensor;

@RestController
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
		if(!sensorRepository.existsById(sensor.getId())) {
			sensorRepository.save(sensor);
			return sensor;
		}
		throw new IllegalArgumentException("The sensorID is already set!!!");
	}

	@RequestMapping(method = RequestMethod.GET)
	private @ResponseBody List<SensorDTO> findAllWithoutTemperature() {
		return sensorRepository.findAllWithoutTemperatures();
		//payload.forEach(p -> temperaturePersister.save(p));

	}

	@RequestMapping(method = RequestMethod.GET, path = "/temperatures")
	private @ResponseBody Iterable<Sensor> findAll() {
		return sensorRepository.findAll();
		//payload.forEach(p -> temperaturePersister.save(p));

	}
	
}
