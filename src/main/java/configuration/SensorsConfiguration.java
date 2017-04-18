package configuration;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.LightSensor;
import model.StreetLamp;
import util.MappingThreadsToLamps;
import util.MappingThreadsToLightSensors;

@Component
public class SensorsConfiguration {

	@Autowired
	StreetLampRepository streetLampRepository;
	@Autowired
	LightSensorRepository lightSensorRepository;

	List<StreetLamp> data = new ArrayList<>();

	public void ConfigSensors() throws IOException, ParseException{
		
//		streetLampRepository.deleteAll();
	//	lightSensorRepository.deleteAll();
    	
        ObjectMapper mapper = new ObjectMapper();
        List<StreetLamp> streetLampList = mapper.readValue(new File("resources/dataset.json"), new TypeReference<List<StreetLamp>>(){});
		this.data = streetLampList;
     //   for(StreetLamp sl: streetLampList){
		//	streetLampRepository.save(sl);
	/*
			LightSensor lightSensor = new LightSensor();
			lightSensor.setLightSensorId(sl.getLampId());
			lightSensor.setLightIntensity(0);
			lightSensor.setAddress(sl.getAddress());
			lightSensor.setTimestamp(0);
			lightSensorRepository.save(lightSensor);
		*/
		//	if(sl.getLampId()==40)
		//		break;
		//}
	}
	
	@PostConstruct
	public void initThreadList() {
		
		try {
			ConfigSensors();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		List<StreetLamp> streetLampList = this.data;
		//streetLampList = streetLampRepository.findAll();
		
		//List<LightSensor> sensorLightList = null;
		//sensorLightList = lightSensorRepository.findAll();

		int i;
		for (i=0; i<streetLampList.size();i++){
			StreetLampThread tl = new StreetLampThread(streetLampList.get(i));
			System.out.println(streetLampList.get(i).getCity());
			MappingThreadsToLamps.getInstance().put(streetLampList.get(i).getLampId(), tl);
			tl.start();
			if(i == 10)
				break;
		/*
			LightSensorThread ts = new LightSensorThread(sensorLightList.get(i));					
			MappingThreadsToLightSensors.getInstance().put(sensorLightList.get(i).getLightSensorId(), ts);
			ts.start();
*/
		}
	}
	
	@PreDestroy
	public void destroyThreadList() {
	
		HashMap<Long, StreetLampThread> tmp = MappingThreadsToLamps.getInstance();
		for (Object value : tmp.values()){
			StreetLampThread t = (StreetLampThread) value;
			t.interrupt();
		}
		/*
		HashMap<Long, LightSensorThread> tmps = MappingThreadsToLightSensors.getInstance();
		for (Object value : tmps.values()){
			LightSensorThread ts = (LightSensorThread) value;
			ts.interrupt();
		}*/
		
	}
	
}
