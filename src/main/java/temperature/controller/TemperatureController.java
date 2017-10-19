package temperature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import temperature.database.TemperatureRepository;
import temperature.model.Temperature;

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
			temperature.setDate(new Date());
		} else {
			temperature.setDate(date);
		}
		temperature.setTemperature(temp);
		return temperaturePersister.save(temperature);
	}

	@RequestMapping(method = RequestMethod.GET)
	private @ResponseBody List<Temperature> getAllTemperatures() {
		ArrayList<Temperature> temperatureList = new ArrayList<>();
		temperaturePersister.findAll().forEach(temperatureList::add);
		return temperatureList;
	}

}
