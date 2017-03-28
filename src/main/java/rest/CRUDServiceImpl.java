package rest;

import model.StreetLamp;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import configuration.MappingThreadsLamps;
import configuration.StreetLampRepository;
import configuration.StreetLampThread;

@Service("CRUDService")
public class CRUDServiceImpl implements CRUDService{

	@Autowired
	StreetLampRepository streetLampRepository;

	@Override
	public ResponseEntity<JSONObject> insertStreetLamp(DTO request) {
		
		String id = request.getId();
		String position = request.getPosition();
		String ligthIntensity = request.getLigthIntensity();
		String bulbModel = request.getBulbModel();
		String powerConsumption = request.getPowerConsumption();
		String state = request.getState();
		String lastSubstitutionDate = request.getLastSubstitutionDate();

		//temp fix		
		StreetLamp streetLamp = new StreetLamp(id, position, ligthIntensity, bulbModel, powerConsumption, 
											   state, lastSubstitutionDate);
		
		streetLampRepository.save(streetLamp);
		
		//create new thread
		StreetLampThread t = new StreetLampThread(streetLamp);					
		MappingThreadsLamps.getInstance().put(id, t);
		
		t.start();
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "InsertOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
	public ResponseEntity<JSONObject> deleteStreetLamp(DTO request) {

		String id = request.getId();
		streetLampRepository.delete(id);
		
		//stopThread()
		StreetLampThread t = (StreetLampThread) MappingThreadsLamps.getInstance().get(id);
		MappingThreadsLamps.getInstance().remove(id);
		t.setStop(true);
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "DeleteOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
			
	}
	
	public ResponseEntity<JSONObject> updateStreetLamp(DTO request) {
		
		String id = request.getId();
		String intensityAdjustment = request.getIntensityAdjustment();
		
		//update thread ligthIntensityAdjustment
		StreetLampThread t = (StreetLampThread) MappingThreadsLamps.getInstance().get(id);
		t.setLigthIntensityAdjustment(Double.parseDouble(intensityAdjustment));
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "UpdateOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
}
