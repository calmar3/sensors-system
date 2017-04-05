package configuration;

import java.util.List;

import model.LigthSensor;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LightSensorRepository extends MongoRepository<LigthSensor, String>{
	
	public List<LigthSensor> findAll();
	public LigthSensor findById(String id);
	@SuppressWarnings("unchecked")
	public LigthSensor save(LigthSensor sensorLight);
	public void deleteAll();
	public void delete(String id);

}
