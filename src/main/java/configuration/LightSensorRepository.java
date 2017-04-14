package configuration;

import java.util.List;

import model.LightSensor;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LightSensorRepository extends MongoRepository<LightSensor, String>{
	
	public List<LightSensor> findAll();
	public LightSensor findByLightSensorId(long id);
	@SuppressWarnings("unchecked")
	public LightSensor save(LightSensor sensorLight);
	public void deleteAll();
	public void deleteByLightSensorId(long id);

}
