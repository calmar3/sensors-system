package model;


import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StreetLamp{

	@Id
	private String id;	
	private String position;
	private String ligthIntensity;
	private String bulbModel;	
	private String powerConsumption;	
	private String state;
	private String lastSubstitutionDate;
	
	public StreetLamp(){}
	
	public StreetLamp(String id, String position, String ligthIntensity, String bulbModel, String powerConsumption, String state, String lastSubstitutionDate){
		this.id = id;
		this.position = position;
		this.ligthIntensity = ligthIntensity;
		this.bulbModel= bulbModel;
		this.powerConsumption = powerConsumption;
		this.state = state;
		this.lastSubstitutionDate = lastSubstitutionDate;
	}

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

	public void setlastSubstitutionDate(String lastSubstitutionDate) {
		this.lastSubstitutionDate = lastSubstitutionDate;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject toJSONObject(StreetLamp l) throws IllegalArgumentException, IllegalAccessException {
		
		JSONObject jo = new JSONObject();
			
			jo.put("bulbModel", l.getBulbModel());
			jo.put("ligthIntensity", l.getLigthIntensity().toString());
			jo.put("id", l.getId());
			jo.put("position", l.getPosition());
			jo.put("powerConsumption", l.getPowerConsumption());
			jo.put("state", l.getState());
			jo.put("lastSubstitutionDate", l.getLastSubstitutionDate());
		
		return jo;
	}
	
	@SuppressWarnings("static-access")
	public static String listToJSONOBjectList(List<StreetLamp> logList) throws IllegalArgumentException, IllegalAccessException {
		String s="{[";
		for(StreetLamp l : logList) 
			s += l.toJSONObject(l).toString();		
		return s+"]}";
	}
	
}
