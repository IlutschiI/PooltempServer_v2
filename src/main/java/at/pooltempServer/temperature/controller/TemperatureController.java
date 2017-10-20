package at.pooltempServer.temperature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.pooltempServer.temperature.database.TemperatureRepository;
import at.pooltempServer.temperature.model.Temperature;

@Controller
@RequestMapping("/temperature")
public class TemperatureController {

	@Autowired
	private TemperatureRepository temperaturePersister;

	@RequestMapping(method = RequestMethod.POST)
	private @ResponseBody Temperature addTemperature(@RequestParam(name = "temp", required = true) double temp,
			@RequestParam(name = "date", required = false) Date date) {
		Temperature temperature = new Temperature();
		if (date == null) {
			temperature.setTime(new Date());
		} else {
			temperature.setTime(date);
		}
		temperature.setTemperature(temp);
		return temperaturePersister.save(temperature);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/addAll")
	private @ResponseBody String addAllTemperature(@RequestBody List<Temperature> payload) {
		payload.forEach(p -> temperaturePersister.save(p));

		return "OK";
	}

	@RequestMapping(method = RequestMethod.GET)
	private @ResponseBody List<Temperature> getAllTemperatures() {
		ArrayList<Temperature> temperatureList = new ArrayList<>();
		temperaturePersister.findAll().forEach(temperatureList::add);
		return temperatureList;
	}

}
