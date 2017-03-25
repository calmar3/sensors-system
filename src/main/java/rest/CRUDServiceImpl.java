package rest;

import javax.inject.Inject;

import model.StreetLamp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import configuration.Addresses;
import configuration.MappingThreadsLamps;
import configuration.StreetLampRepository;
import configuration.StreetLampThread;

@Service("LoginService")
public class CRUDServiceImpl implements CRUDService{
	
	@Autowired
	MongoTemplate mongoDb;
	@Inject
	StreetLampRepository streetLampRepository;

	@Override
	public void insertStreetLamp(DTO request) {
		
		JSONObject jo = null;
		JSONParser parser = new JSONParser();
		
		try {
			jo = (JSONObject) parser.parse(request.getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String id = jo.get("id").toString();
		String position = jo.get("position").toString();
		String ligthIntensity = jo.get("ligthIntensity").toString();
		String bulbMode = jo.get("bulbMode").toString();
		String powerConsumption = jo.get("powerConsumption").toString();
		String state = jo.get("state").toString();
		String lastSubstitutionDate = jo.get("lastSubstitutionDate").toString();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id).and("position").is(position).and("ligthIntensity").is(ligthIntensity)
													 .and("bulbMode").is(bulbMode).and("powerConsumption").is(powerConsumption)
													 .and("state").is(state).and("lastSubstitutionDate").is(lastSubstitutionDate));
	
		mongoDb.save(jo, Addresses.database);
		
		//create new thread
		StreetLamp streetLamp = null;
		streetLamp = streetLampRepository.findById(id);
	
		StreetLampThread sl = new StreetLampThread(streetLamp);					
		MappingThreadsLamps.getInstance().put(streetLamp.getId(), sl);
		
		sl.start();
	}
	
	public void updateStreetLamp(DTO request) {
		
		JSONObject jo = null;
		JSONParser parser = new JSONParser();
		
		try {
			jo = (JSONObject) parser.parse(request.getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String id = jo.get("id").toString();
		String intensityAdjustment = jo.get("intensityAdjustment").toString();
		
		//update thread ligthIntensityAdjustment
		StreetLampThread t = (StreetLampThread) MappingThreadsLamps.getInstance().get(id);
		t.setLigthIntensityAdjustment(Double.parseDouble(intensityAdjustment));
	}
	
	public void deleteStreetLamp(DTO request) {

		JSONObject jo = null;
		JSONParser parser = new JSONParser();
		
		try {
			jo = (JSONObject) parser.parse(request.getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Query query = new Query();
		String id = jo.get("id").toString();
		
		query.addCriteria(Criteria.where("id").is(id));
	
		mongoDb.find(query, JSONObject.class, "dbname");
		mongoDb.remove(query, "dbname");
		
		//stopThread()
		StreetLampThread t = (StreetLampThread) MappingThreadsLamps.getInstance().get(id);
		t.setStop(true);
			
	}
	
}
