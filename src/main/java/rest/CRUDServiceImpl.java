package rest;

import java.util.List;

import model.LightSensor;
import model.StreetLamp;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import util.MappingThreadsToLamps;
import util.MappingThreadsToLightSensors;
import configuration.LightSensorRepository;
import configuration.LightSensorThread;
import configuration.StreetLampRepository;
import configuration.StreetLampThread;

@Service("CRUDService")
public class CRUDServiceImpl implements CRUDService{

	@Autowired
	StreetLampRepository streetLampRepository;
	
	@Autowired
	LightSensorRepository sensorLightRepository;


	@Override
	public ResponseEntity<JSONObject> insertStreetLamp(DTO request) {

		long id = request.getLampId();
		double lightIntensity = request.getLightIntensity();
		String address = request.getAddress();
		String latitude = request.getLatitude();
		String longitude = request.getLongitude();
		String model = request.getModel();
		double consumption = request.getConsumption();
		String city = request.getCity();
		long lastSubstitutionDate = request.getLastSubstitutionDate();
		List<Long> cellId = request.getCellId();
		long timestamp = 0;
		long residualLifeTime = 0;
		boolean stateOn = request.isStateOn();
		
		StreetLamp streetLamp = new StreetLamp(id, cellId, consumption, 
											   address, city, longitude, latitude, 
											   timestamp, lastSubstitutionDate, residualLifeTime,
											   stateOn, lightIntensity, model);
		
		streetLampRepository.save(streetLamp);
		
		LightSensor sensorLight= new LightSensor(id, 0, address, 0);
			
		
		sensorLightRepository.save(sensorLight);

		//create new thread
		StreetLampThread t = new StreetLampThread(streetLamp);					
		MappingThreadsToLamps.getInstance().put(id, t);
		
		t.start();
		
		//create new sensor thread

		LightSensorThread ts = new LightSensorThread(sensorLight);					
		MappingThreadsToLightSensors.getInstance().put(id, ts);
		
		ts.start();
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "InsertOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
	public ResponseEntity<JSONObject> deleteStreetLamp(DTO request) {

		long id = request.getLampId();
		streetLampRepository.deleteByLampId(id);
			
		//stopThread()
		StreetLampThread t = (StreetLampThread) MappingThreadsToLamps.getInstance().get(id);
		MappingThreadsToLamps.getInstance().remove(id);
		t.setStop(true);
		
		sensorLightRepository.deleteByLightSensorId(id);
		
		//stopThread() sensor
		LightSensorThread ts = (LightSensorThread) MappingThreadsToLightSensors.getInstance().get(id);
		MappingThreadsToLightSensors.getInstance().remove(id);
		ts.setStop(true);
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "DeleteOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
			
	}
	
	public ResponseEntity<JSONObject> updateStreetLamp(DTO request) {
		
		Long id = request.getLampId();
		double intensityAdjustment = request.getIntensityAdjustment();
		
		//update thread lightIntensityAdjustment
		StreetLampThread t = (StreetLampThread) MappingThreadsToLamps.getInstance().get(id);
		t.setLightIntensityAdjustment(intensityAdjustment);
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "UpdateOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
}
