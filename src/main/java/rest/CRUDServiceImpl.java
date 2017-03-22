package rest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import configuration.Addresses;

@Service("LoginService")
public class CRUDServiceImpl implements CRUDService{
	
	@Autowired
	MongoTemplate mongoDb;

	@Override
	public void insertStreetLamp(DTOCRUDRequest request) {
		
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
		
		//createNewThread()
	}
	
	public void updateStreetLamp(DTOCRUDRequest request) {
		
		//notify thread
		
	}
	
	public void deleteStreetLamp(DTOCRUDRequest request) {

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
			
	}
	
}
