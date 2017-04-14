package model;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StreetLamp{

	@Id
	private long lampId;
	private List<Long> cellId;
	private double consumption;	
	private String address;
	private String city;
	private String longitude;
	private String latitude;
	private long timestamp;
	private long lastSubstitutionDate;
	private long residualLifeTime;
	private boolean stateOn;
	private double lightIntensity;
	private String model;	
		
	public StreetLamp(){}
	
	public StreetLamp(long lampId, List<Long> cellId, double consumption, 
					  String address, String city, String longitude, String latitude, 
					  long timestamp, long lastSubstitutionDate, long residualLifeTime,
					  boolean stateOn, double lightIntensity, String model){
		this.lampId = lampId;
		this.cellId = cellId;
		this.consumption = consumption;
		this.address = address;
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
		this.timestamp = timestamp;
		this.lastSubstitutionDate = lastSubstitutionDate;
		this.residualLifeTime = residualLifeTime;
		this.stateOn = stateOn;
		this.lightIntensity = lightIntensity;
		this.model = model;
	}

	public long getLampId() {
		return lampId;
	}

	public void setLampId(long lampId) {
		this.lampId = lampId;
	}

	public List<Long> getCellId() {
		return cellId;
	}

	public void setCellId(List<Long> cellId) {
		this.cellId = cellId;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getLastSubstitutionDate() {
		return lastSubstitutionDate;
	}

	public void setLastSubstitutionDate(long lastSubstitutionDate) {
		this.lastSubstitutionDate = lastSubstitutionDate;
	}

	public long getResidualLifeTime() {
		return residualLifeTime;
	}

	public void setResidualLifeTime(long residualLifeTime) {
		this.residualLifeTime = residualLifeTime;
	}

	public boolean isStateOn() {
		return stateOn;
	}

	public void setStateOn(boolean stateOn) {
		this.stateOn = stateOn;
	}

	public double getLightIntensity() {
		return lightIntensity;
	}

	public void setLightIntensity(double lightIntensity) {
		this.lightIntensity = lightIntensity;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJSONObject(StreetLamp l) throws IllegalArgumentException, IllegalAccessException {
		
		JSONObject jo = new JSONObject();
			
			jo.put("lampId", l.getLampId());
			jo.put("cellId", l.getCellId());
			jo.put("consumption", l.getConsumption());
			jo.put("address", l.getAddress());
			jo.put("longitude", l.getLongitude());
			jo.put("latitude", l.getLatitude());
			jo.put("timestamp", l.getTimestamp());
			jo.put("lastSubstitutionDate", l.getLastSubstitutionDate());
			jo.put("residualLifeTime", l.getResidualLifeTime());
			jo.put("stateOn", l.isStateOn());
			jo.put("lightIntensity", l.getLightIntensity());
			jo.put("model", l.getModel());
		
		return jo;
	}

}
