package rest;

import org.springframework.http.ResponseEntity;

public interface CRUDService {
	
	public ResponseEntity<DTO> insertStreetLamp(DTO request);
	public ResponseEntity<DTO> deleteStreetLamp(DTO request);
	public ResponseEntity<DTO> updateStreetLamp(DTO request);

}
