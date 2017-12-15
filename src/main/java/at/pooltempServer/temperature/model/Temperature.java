package at.pooltempServer.temperature.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Temperature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date time;
	private double temperature;
	private TemperatureType temperatureType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date date) {
		this.time = date;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public TemperatureType getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(TemperatureType temperatureType) {
		this.temperatureType = temperatureType;
	}

}
