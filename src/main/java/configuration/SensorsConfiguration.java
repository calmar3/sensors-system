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

import model.StreetLamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorsConfiguration {

	@Autowired
	StreetLampRepository streetLampRepository;
	private String numLamp;
	private String address;
	private String model;
	
	public String getNumLamp() {
		return numLamp;
	}

	public void setNumLamp(String numLamp) {
		this.numLamp = numLamp;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	
	
	public void ConfigLamp() throws IOException, ParseException{
		
		streetLampRepository.deleteAll();
		
		File file = new File("resources/Sensors");
		Scanner s = new Scanner(file);
    	
		int id = 1;
		while (s.hasNext()){
			String [] line = s.nextLine().split("\\s+");
		    
		    if(line[0].startsWith("num_lamp:")){
		    	setNumLamp(line[0].substring(9));
        	}else{
        		s.close();
		    	throw new InvalidObjectException("Invalid configuration file!");
        	}
	        for(String i:line){
	        	if(i == line[0])
	        		continue;
	        	else if(i.startsWith("address:")){
	        		setAddress(i.substring(8).replace("_", " "));
	        	}
	        	else if(i.startsWith("model:")){
	        		setModel(i.substring(6));
	        	}
	        }
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    		String tmp = dateFormat.format(new Date());
    		Date timeStamp = dateFormat.parse(tmp);
    		
    		int j = Integer.parseInt(numLamp);
    		while(j>0){    		
	    		StreetLamp lp = new StreetLamp();
	    		lp.setBulbModel(getModel());
	    		lp.setId(""+Integer.toString(id)+"");
	    		lp.setLigthIntensity("0");
	    		lp.setPosition(getAddress()+", "+j+"");
	    		lp.setPowerConsumption("0");
	    		lp.setState("OFF");
	    		lp.setlastSubstitutionDate(""+timeStamp+"");
	    		streetLampRepository.save(lp);
	    		
	    		id++;
	    		j--;
    		}
		}
		s.close();			
	}
	
	@PostConstruct
	public void initThreadList() {
		
		try {
			ConfigLamp();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		List<StreetLamp> streetLampList = null;
		streetLampList = streetLampRepository.findAll();
		
		for (StreetLamp lamp : streetLampList){
			StreetLampThread t = new StreetLampThread(lamp);					
			MappingThreadsLamps.getInstance().put(lamp.getId(), t);
			
			t.start();
		}
	}
	
	@PreDestroy
	public void destroyThreadList() {
	
		HashMap<String, StreetLampThread> tmp = MappingThreadsLamps.getInstance();
		for (Object value : tmp.values()){
			StreetLampThread t = (StreetLampThread) value;
			t.interrupt();
		}
	}
	
}
