package configuration;

import java.util.List;

import javax.inject.Inject;

import model.StreetLamp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import repository.StreetLampRepository;

@Configuration
public class SensorsConfiguration {
    
	@Inject
	private StreetLampRepository streetLampRepository;
	
	/*@Bean
	public void initDB() {
		
		//doSomething
		 
	}*/
	
	@Bean
	public void initThreadList() {
		
		List<StreetLamp> streetLampList = null;
		streetLampList = streetLampRepository.findAll();
		
		for (StreetLamp lamp : streetLampList){
			StreetLampThread sl = new StreetLampThread(lamp);					
			MappingThreadsLamps.getInstance().put(lamp.getId(), sl);
			
			sl.start();
		}
	}
	
}
