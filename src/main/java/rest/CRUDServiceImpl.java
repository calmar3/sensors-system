package rest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import configuration.LightSensorRepository;
import configuration.LigthSensorThread;
import configuration.StreetLampRepository;
import configuration.StreetLampThread;
import model.LigthSensor;
import model.StreetLamp;
import util.MappingThreadsToLamps;
import util.MappingThreadsToLightSensors;

@Service("CRUDService")
public class CRUDServiceImpl implements CRUDService{

	@Autowired
	StreetLampRepository streetLampRepository;
	LightSensorRepository sensorLightRepository;


	@Override
	public ResponseEntity<JSONObject> insertStreetLamp(DTO request) {
		
		String id = request.getId();
		String position = request.getPosition();
		String ligthIntensity = request.getLigthIntensity();
		String bulbModel = request.getBulbModel();
		String powerConsumption = request.getPowerConsumption();
		String state = request.getState();
		String lastSubstitutionDate = request.getLastSubstitutionDate();
	
		StreetLamp streetLamp = new StreetLamp(id, position, ligthIntensity, bulbModel, powerConsumption, 
											   state, lastSubstitutionDate);
		
		streetLampRepository.save(streetLamp);
		
		LigthSensor sensorLight= new LigthSensor(id, position,"0.0");
		
		sensorLightRepository.save(sensorLight);

		//create new thread
		StreetLampThread t = new StreetLampThread(streetLamp);					
		MappingThreadsToLamps.getInstance().put(id, t);
		
		t.start();
		
		//create new sensor thread

		LigthSensorThread ts = new LigthSensorThread(sensorLight);					
		MappingThreadsToLightSensors.getInstance().put(id, ts);
		
		ts.start();
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "InsertOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
	public ResponseEntity<JSONObject> deleteStreetLamp(DTO request) {

		String id = request.getId();
		streetLampRepository.delete(id);
			
		//stopThread()
		StreetLampThread t = (StreetLampThread) MappingThreadsToLamps.getInstance().get(id);
		MappingThreadsToLamps.getInstance().remove(id);
		t.setStop(true);
		
		sensorLightRepository.delete(id);
		
		//stopThread() sensor
		LigthSensorThread ts = (LigthSensorThread) MappingThreadsToLightSensors.getInstance().get(id);
		MappingThreadsToLightSensors.getInstance().remove(id);
		ts.setStop(true);
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "DeleteOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
			
	}
	
	public ResponseEntity<JSONObject> updateStreetLamp(DTO request) {
		
		String id = request.getId();
		String intensityAdjustment = request.getIntensityAdjustment();
		
		//update thread ligthIntensityAdjustment
		StreetLampThread t = (StreetLampThread) MappingThreadsToLamps.getInstance().get(id);
		t.setLigthIntensityAdjustment(Double.parseDouble(intensityAdjustment));
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "UpdateOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
}
