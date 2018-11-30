package at.pooltempServer.temperature.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.pooltempServer.temperature.model.Temperature;

import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

 List<Temperature> findTemperatureBySensorID(String sensorId);

}
