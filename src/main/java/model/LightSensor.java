package model;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LightSensor{

	@Id
	private long lightSensorId;	
	private double lightIntensity;
	private String address;
	private long timestamp;	
		
	public LightSensor(){}
	
	public LightSensor(long lightSensorId, double lightIntensity, String address, long timestamp){
		this.setLightSensorId(lightSensorId);
		this.setLightIntensity(lightIntensity);
		this.setAddress(address);
		this.setTimestamp(timestamp);
	}

	public long getLightSensorId() {
		return lightSensorId;
	}

	public void setLightSensorId(long lightSensorId) {
		this.lightSensorId = lightSensorId;
	}

	public double getLightIntensity() {
		return lightIntensity;
	}

	public void setLightIntensity(double lightIntensity) {
		this.lightIntensity = lightIntensity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJSONObject(LightSensor l) throws IllegalArgumentException, IllegalAccessException {
		
		JSONObject jo = new JSONObject();
			
			jo.put("lightSensorId", l.getLightSensorId());
			jo.put("lightIntensity", l.getLightIntensity());
			jo.put("address", l.getAddress());
			jo.put("timestamp", l.getTimestamp());
			
		return jo;
	}

}
