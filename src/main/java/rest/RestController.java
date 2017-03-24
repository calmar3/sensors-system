package rest;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void insert(@RequestBody DTO request) {
		
		crudService.insertStreetLamp(request);
	
	}
	
	@RequestMapping(value = "/updateStreetLamp/", method = RequestMethod.POST) 
	public void update(@RequestBody DTO request){
	
		crudService.updateStreetLamp(request);
	
	}
	
	@RequestMapping(value = "/deleteStreetLamp/", method = RequestMethod.POST) 
	public void delete(@RequestBody DTO request){
		
		crudService.deleteStreetLamp(request);
	
	}
	
}
