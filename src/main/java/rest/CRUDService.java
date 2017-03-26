package rest;

import org.springframework.http.ResponseEntity;
import org.json.simple.JSONObject;

public interface CRUDService {
	
	public ResponseEntity<JSONObject> insertStreetLamp(DTO request);
	public ResponseEntity<JSONObject> deleteStreetLamp(DTO request);
	public ResponseEntity<JSONObject> updateStreetLamp(DTO request);

}
