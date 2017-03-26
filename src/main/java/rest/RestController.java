package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class RestController {
	
	@Autowired
	CRUDService crudService;
	
	@RequestMapping(value = "/insertNewStreetLamp/", method = RequestMethod.POST)
	public ResponseEntity<DTO> insert(@RequestBody DTO request) {

		return crudService.insertStreetLamp(request);
	
	}
	
	@RequestMapping(value = "/deleteStreetLamp/", method = RequestMethod.POST) 
	public ResponseEntity<DTO> delete(@RequestBody DTO request){
		
		return crudService.deleteStreetLamp(request);
	
	}
	
	@RequestMapping(value = "/AdjustStreetLampLigthIntensity/", method = RequestMethod.POST) 
	public ResponseEntity<DTO> update(@RequestBody DTO request){
	
		return crudService.updateStreetLamp(request);
	
	}
	
}
