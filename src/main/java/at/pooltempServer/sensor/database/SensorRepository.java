package at.pooltempServer.sensor.database;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.pooltempServer.sensor.model.Sensor;

public interface SensorRepository extends CrudRepository<Sensor, String> {
	
	@Query("Select distinct s.id from Sensor s")
	List<String> findSensorIDs();
	
}
