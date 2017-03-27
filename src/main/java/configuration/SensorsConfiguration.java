package configuration;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import model.Street;
import model.StreetLamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorsConfiguration {

	@Autowired
	StreetLampRepository streetLampRepository;
	private ArrayList<Street> streetList;

	public ArrayList<Street> getStreetList() {
		return streetList;
	}

	public void setStreetList(ArrayList<Street> streetList) {
		this.streetList = streetList;
	}
	
	public List<Street> readConfig() throws IOException{
		
		File file = new File("resources/Sensors");
		Scanner s = new Scanner(file);

		ArrayList<Street> list = new ArrayList<Street>();
		while (s.hasNext()){
			Street street = new Street();
			String [] line = s.nextLine().split("\\s+");
		    
		    if(line[0].startsWith("num_lamp:")){
		    	street.setNumLamp(line[0].substring(9));
        	}else{
        		s.close();
		    	throw new InvalidObjectException("Invalid configuration file!");
        	}
	        for(String i:line){
	        	if(i == line[0])
	        		continue;
	        	else if(i.startsWith("address:")){
	        		street.setAddress(i.substring(8).replace("_", " "));
	        	}
	        	else if(i.startsWith("model:")){
	        		street.setModel(i.substring(6));
	        	}
	        	else if(i.startsWith("consumption:")){
	        		street.setConsumption(i.substring(12));
	        	}
	        	else if(i.startsWith("intensity_mrn:")){
	        		street.setIntensityMrn(i.substring(14));
	        	}
	        	else if(i.startsWith("intensity_aft:")){ 
	        		street.setIntensityAft(i.substring(14));		
	        	}
			    else if(i.startsWith("intensity_evng:")){
			    	street.setIntensityEvng(i.substring(15));
	        	}
			    else if(i.startsWith("intensity_nght:")){
			    	street.setIntensityNght(i.substring(15));
	        	}
	        }
	        list.add(street);
		}
		s.close();	
		this.setStreetList(list);
		
		return this.streetList;
	}
	
	public void ConfigLamp() throws IOException, ParseException{

		streetLampRepository.deleteAll();
		List<Street> streetList = null;
		
		streetList = readConfig();
		
    	int id = 1;
    	for(Street street : streetList){
    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    		String tmp = dateFormat.format(new Date());
    		Date timeStamp = dateFormat.parse(tmp);
    		
    		int j = Integer.parseInt(street.getNumLamp());
    		while(j>0){    		
	    		StreetLamp lp = new StreetLamp();
	    		lp.setBulbModel(street.getModel());
	    		lp.setId(""+Integer.toString(id)+"");
	    		lp.setLigthIntensity("0");
	    		lp.setPosition(street.getAddress()+", "+j+"");
	    		lp.setPowerConsumption("0");
	    		lp.setState("OFF");
	    		lp.setlastSubstitutionDate(""+timeStamp+"");
	    		lp.setStreet(street);
	    		streetLampRepository.save(lp);
	    		
	    		id++;
	    		j--;
    		}
    	}
    	
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
