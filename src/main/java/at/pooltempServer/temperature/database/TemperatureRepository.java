package at.pooltempServer.temperature.database;

import at.pooltempServer.sensor.model.Sensor;
import at.pooltempServer.temperature.model.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    Temperature findTopBySensorEqualsOrderByTemperatureDesc(Sensor sensor);

    Temperature findTopBySensorEqualsOrderByTemperatureAsc(Sensor sensor);

    List<Temperature> findAllBySensorEqualsAndTimeAfter(Sensor sensor, Date time);

    @Query("select count(t) from Temperature t")
    long countEntries();

    long countAllBySensorEquals(Sensor sensor);

    List<Temperature> findAllBySensorEquals(Sensor sensor);

    Temperature findFirstBySensorEqualsOrderByTimeDesc(Sensor sensor);
}
