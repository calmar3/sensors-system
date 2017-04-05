package model;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LigthSensor{

	@Id
	private String id;	
	private String position;
	private String ligthIntensity;
		
	public LigthSensor(){}
	
	
	public LigthSensor(String id, String position, String ligthIntensity){
		this.id = id;
		this.position = position;
		this.ligthIntensity = ligthIntensity;

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
	
	
	@SuppressWarnings("unchecked")
	public static JSONObject toJSONObject(LigthSensor l) throws IllegalArgumentException, IllegalAccessException {
		
		JSONObject jo = new JSONObject();
			
			jo.put("ligthIntensity", l.getLigthIntensity().toString());
			jo.put("id", l.getId());
			jo.put("position", l.getPosition());
		
		return jo;
	}

}
