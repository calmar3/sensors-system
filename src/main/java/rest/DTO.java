package rest;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private String id;
	private String position;
	private String ligthIntensity;
	private String bulbModel;
	private String powerConsumption;
	private String state;
	private String lastSubstitutionDate;
	private String intensityAdjustment;
	
	public DTO(){};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLigthIntensity() {
		return ligthIntensity;
	}

	public void setLigthIntensity(String ligthIntensity) {
		this.ligthIntensity = ligthIntensity;
	}

	public String getBulbModel() {
		return bulbModel;
	}

	public void setBulbModel(String bulbModel) {
		this.bulbModel = bulbModel;
	}

	public String getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(String powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLastSubstitutionDate() {
		return lastSubstitutionDate;
	}

	public void setLastSubstitutionDate(String lastSubstitutionDate) {
		this.lastSubstitutionDate = lastSubstitutionDate;
	}

	public String getIntensityAdjustment() {
		return intensityAdjustment;
	}

	public void setIntensityAdjustment(String intensityAdjustment) {
		this.intensityAdjustment = intensityAdjustment;
	}
}
