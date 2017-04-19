package rest;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private long lampId;
	private double consumption;	
	private String address;
	private String longitude;
	private String latitude;
	private long timestamp;
	private long lastSubstitutionDate;
	private boolean stateOn;
	private double lightIntensity;
	private String model;
	private double intensityAdjustment;
	private String city;
	
	public DTO(){}

	public long getLampId() {
		return lampId;
	}

	public void setLampId(long lampId) {
		this.lampId = lampId;
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

	public double getIntensityAdjustment() {
		return intensityAdjustment;
	}

	public void setIntensityAdjustment(double intensityAdjustment) {
		this.intensityAdjustment = intensityAdjustment;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	};

}
