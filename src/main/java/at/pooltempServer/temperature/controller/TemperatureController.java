package at.pooltempServer.temperature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import at.pooltempServer.temperature.model.TemperatureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import at.pooltempServer.temperature.database.TemperatureRepository;
import at.pooltempServer.temperature.model.Temperature;

@CrossOrigin(origins="*")
@Controller
@RequestMapping("/temperature")
public class TemperatureController {

	@Autowired
	private TemperatureRepository temperaturePersister;

	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,path="/new")
	private @ResponseBody Temperature addTemperature(@RequestBody Temperature temp) {
		if(temp.getTime()==null) {
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
		temperaturePersister.findAll().forEach(t->temperatureList.add(mapToDTO(t)));
		return temperatureList;
	}
	
	@RequestMapping(method=RequestMethod.GET,params="since")
	private @ResponseBody List<Temperature> getTemperatureSince(@RequestParam(name="since", required=true) long since){
		ArrayList<Temperature> temperatureList=new ArrayList<>();
		temperaturePersister.findAll().forEach(temperatureList::add);
		return temperatureList.stream().filter(t->t.getTime().after(new Date(since))).collect(Collectors.toList());
	}

	private @ResponseBody Temperature getLatestTemperature(@RequestParam(name="sensor", required = true)String sensor){
	    return null;
    }

    private TemperatureDTO mapToDTO(Temperature temperature){
		TemperatureDTO temperatureDTO = new TemperatureDTO();
		temperatureDTO.setSensorID(temperature.getSensorID());
		temperatureDTO.setTemperature(temperature.getTemperature());
		temperatureDTO.setTime(temperature.getTime());
		return temperatureDTO;
	}

}
