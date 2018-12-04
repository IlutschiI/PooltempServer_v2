package at.pooltempServer.temperature.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.pooltempServer.temperature.model.Temperature;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    List<Temperature> findTemperatureBySensorID(String sensorId);

    Temperature findFirstBySensorIDOrderByTemperatureDesc(String sensorId);

    Temperature findFirstBySensorIDOrderByTemperatureAsc(String sensorId);

    List<Temperature> findTemperatureByTimeBetween(Date startDate, Date endDate);

    List<Temperature> findTemperatureBySensorIDAndTimeBetween(String sensorId, Date startDate, Date endDate);
}
