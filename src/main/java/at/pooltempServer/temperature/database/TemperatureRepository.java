package at.pooltempServer.temperature.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.pooltempServer.temperature.model.Temperature;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

}
