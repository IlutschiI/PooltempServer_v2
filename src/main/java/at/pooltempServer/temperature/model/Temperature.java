package at.pooltempServer.temperature.model;

import at.pooltempServer.sensor.model.Sensor;

import java.util.Date;

import javax.persistence.*;

@Entity
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date time;
    private double temperature;
    @ManyToOne
    @JoinColumn(name = "sensorid")
    private Sensor sensor;

    public Temperature() {
    }

    public Temperature(Date time, double temperature) {
        this.time = time;
        this.temperature = temperature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
