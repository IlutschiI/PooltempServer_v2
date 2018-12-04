package at.pooltempServer.sensor.model;

import at.pooltempServer.temperature.model.Temperature;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public interface SensorDTO {

    String getId();

    String getName();

}
