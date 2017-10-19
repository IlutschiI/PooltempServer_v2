package temperature.database;

import org.springframework.data.repository.CrudRepository;

import temperature.model.Temperature;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

}
