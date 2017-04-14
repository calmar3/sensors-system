package configuration;

import java.util.List;

import model.StreetLamp;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StreetLampRepository extends MongoRepository<StreetLamp, String>{
	
	public List<StreetLamp> findAll();
	public StreetLamp findByLampId(long id);
	@SuppressWarnings("unchecked")
	public StreetLamp save(StreetLamp streetLamp);
	public void deleteAll();
	public void deleteByLampId(long id);

}
