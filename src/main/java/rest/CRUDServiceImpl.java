package rest;

import model.LightSensor;
import model.StreetLamp;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import thread.LightSensorThread;
import thread.StreetLampThread;
import util.MappingThreadsToLamps;
import util.MappingThreadsToLightSensors;
import configuration.LightSensorRepository;
import configuration.SensorsConfiguration;
import configuration.StreetLampRepository;

@Service("CRUDService")
public class CRUDServiceImpl implements CRUDService{

	@Autowired
	StreetLampRepository streetLampRepository;
	@Autowired
	LightSensorRepository sensorLightRepository;
	
	private StreetLampThread lastLampThread = null;
	private LightSensorThread lastLigthSensorThread = null;


	@Override
	public ResponseEntity<JSONObject> insertStreetLamp(DTO request) {

		//parse and save streetLamp
		long id = request.getLampId();
		double lightIntensity = request.getLightIntensity();
		String address = request.getAddress();
		String latitude = request.getLatitude();
		String longitude = request.getLongitude();
		String model = request.getModel();
		double consumption = request.getConsumption();
		String city = request.getCity();
		long lastSubstitutionDate = request.getLastSubstitutionDate();
		long timestamp = 0;
		long residualLifeTime = 0;
		boolean stateOn = request.isStateOn();
		
		StreetLamp streetLamp = new StreetLamp(id, consumption, 
											   address, city, longitude, latitude, 
											   timestamp, lastSubstitutionDate, residualLifeTime,
											   stateOn, lightIntensity, model);
		streetLampRepository.save(streetLamp);
		
		//parse and save sensorLight
		LightSensor sensorLight= new LightSensor(id, 0, address, 0);
		sensorLightRepository.save(sensorLight);

		//create new lampThread if necessary
		if(this.lastLampThread == null || this.lastLampThread.getStreetLampList().size() > SensorsConfiguration.LAMPS_FOR_THREAD){
			StreetLampThread t = new StreetLampThread();
			t.getStreetLampList().add(streetLamp);
			MappingThreadsToLamps.getInstance().put(id, t);
			t.getListAdjustment().getMappingAdjustmentToLamps().put(id, 1.0);
			this.lastLampThread = t;
			t.start();
		}
		else{
			this.lastLampThread.getListAdjustment().getMappingAdjustmentToLamps().put(id, 1.0);
			this.lastLampThread.getStreetLampList().add(streetLamp);
		}
		
		//create new sensorThread if necessary
		if(this.lastLigthSensorThread == null || this.lastLigthSensorThread.getLightSensorList().size() > SensorsConfiguration.LAMPS_FOR_THREAD){
			LightSensorThread t = new LightSensorThread();
			t.getLightSensorList().add(sensorLight);
			MappingThreadsToLightSensors.getInstance().put(id, t);
			this.lastLigthSensorThread = t;
			t.start();
		}
		else{
			this.lastLigthSensorThread.getLightSensorList().add(sensorLight);
		}
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "InsertOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
	public ResponseEntity<JSONObject> deleteStreetLamp(DTO request) {

		//delete lamp
		long id = request.getLampId();
		streetLampRepository.deleteByLampId(id);
			
		//stop lampThread() if necessary
		StreetLampThread lampThread = (StreetLampThread) MappingThreadsToLamps.getInstance().get(id);
		lampThread.getListAdjustment().getMappingAdjustmentToLamps().remove(id);
		if(lampThread.getStreetLampList().isEmpty()){
			lampThread.setStop(true);
		}
		MappingThreadsToLamps.getInstance().remove(id);
		
		//delete light sensor
		sensorLightRepository.deleteByLightSensorId(id);
		
		//stop lightThread() if necessary
		LightSensorThread lightThread = (LightSensorThread) MappingThreadsToLightSensors.getInstance().get(id);
		if(lightThread.getLightSensorList().isEmpty()){
			lightThread.setStop(true);
		}
		MappingThreadsToLightSensors.getInstance().remove(id);
		
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
		t.getListAdjustment().getMappingAdjustmentToLamps().replace(id, intensityAdjustment);
		
		JSONObject jo = new JSONObject();
		jo.put("responseCode", "UpdateOK");
		ResponseEntity<JSONObject> response = new ResponseEntity<JSONObject>(jo, HttpStatus.OK);
		return response;
	}
	
}
