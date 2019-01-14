package at.pooltempServer.sensor.model;

import at.pooltempServer.temperature.model.Temperature;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensor")
public class Sensor {

	@Id
	private String id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "temperature")
	private List<Temperature> temperatures=new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Temperature> getTemperatures() {
		return temperatures;
	}

	public void setTemperatures(List<Temperature> temperatures) {
		this.temperatures = temperatures;
	}

	public void addTemperature(Temperature temperature){
		temperatures.add(temperature);
	}

	public  void removeTemperature(Temperature temperature){
		temperatures.remove(temperature);
	}
}
