CREATE TABLE sensor (
  id VARCHAR(255) ,
  name VARCHAR(255),
  PRIMARY KEY (id)
);

Create Table temperature (
  id BIGINT AUTO_INCREMENT ,
  time DATE,
  temperature DOUBLE,
  sensorid VARCHAR(255),
  PRIMARY KEY(id),
  CONSTRAINT fk_Tempterature_Sensor FOREIGN KEY (sensorID) REFERENCES sensor(id)
);