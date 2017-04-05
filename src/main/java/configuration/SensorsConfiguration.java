package configuration;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.LigthSensor;
import model.StreetLamp;
import util.MappingThreadsToLamps;
import util.MappingThreadsToLightSensors;

@Component
public class SensorsConfiguration {

	@Autowired
	StreetLampRepository streetLampRepository;
	LightSensorRepository sensorLightRepository;

	public void ConfigSensors() throws IOException, ParseException{
		String numLamp=null;
	    String address=null;
		String model=null;
		
		streetLampRepository.deleteAll();
		sensorLightRepository.deleteAll();

		File file = new File("resources/Sensors");
		Scanner s = new Scanner(file);
    	
		int id = 1;
		while (s.hasNext()){
			String [] line = s.nextLine().split("\\s+");
		    
		    if(line[0].startsWith("num_lamp:")){
		    	numLamp=line[0].substring(9);
        	}else{
        		s.close();
		    	throw new InvalidObjectException("Invalid configuration file!");
        	}
	        for(String i:line){
	        	if(i == line[0])
	        		continue;
	        	else if(i.startsWith("address:")){
	        		address=i.substring(8).replace("_", " ");
	        	}
	        	else if(i.startsWith("model:")){
	        		model=i.substring(6);
	        	}
	        }
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    		String tmp = dateFormat.format(new Date());
    		Date timeStamp = dateFormat.parse(tmp);
    		
    		int j = Integer.parseInt(numLamp);
    		while(j>0){    		
	    		StreetLamp lp = new StreetLamp();
	    		lp.setBulbModel(model);
	    		lp.setId(""+Integer.toString(id)+"");
	    		lp.setLigthIntensity("0");
	    		lp.setPosition(address+", "+j+"");
	    		lp.setPowerConsumption("0");
	    		lp.setState("OFF");
	    		lp.setlastSubstitutionDate(""+timeStamp+"");
	    		streetLampRepository.save(lp);
	    		
	    		LigthSensor sl = new LigthSensor();
	    		sl.setId(""+Integer.toString(id)+"");
	    		sl.setLigthIntensity("0");
	    		sl.setPosition(address+", "+j+"");
	    		sensorLightRepository.save(sl);
	    		id++;
	    		j--;
    		}
		}
		s.close();			
	}
	
	@PostConstruct
	public void initThreadList() {
		
		try {
			ConfigSensors();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		List<StreetLamp> streetLampList = null;
		streetLampList = streetLampRepository.findAll();
		
		List<LigthSensor> sensorLightList = null;
		sensorLightList = sensorLightRepository.findAll();

		int i;
		for (i=0; i<streetLampList.size();i++){
			StreetLampThread tl = new StreetLampThread(streetLampList.get(i));					
			MappingThreadsToLamps.getInstance().put(streetLampList.get(i).getId(), tl);
			tl.start();
			
			LigthSensorThread ts = new LigthSensorThread(sensorLightList.get(i));					
			MappingThreadsToLightSensors.getInstance().put(sensorLightList.get(i).getId(), ts);
			ts.start();

		}
	}
	
	@PreDestroy
	public void destroyThreadList() {
	
		HashMap<String, StreetLampThread> tmp = MappingThreadsToLamps.getInstance();
		for (Object value : tmp.values()){
			StreetLampThread t = (StreetLampThread) value;
			t.interrupt();
		}
		
		HashMap<String, LigthSensorThread> tmps = MappingThreadsToLightSensors.getInstance();
		for (Object value : tmps.values()){
			LigthSensorThread ts = (LigthSensorThread) value;
			ts.interrupt();
		}
		
	}
	
}
