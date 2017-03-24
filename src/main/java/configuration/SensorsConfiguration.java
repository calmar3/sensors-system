package configuration;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

import model.Street;
import model.StreetLamp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import repository.StreetLampRepository;

@Configuration
public class SensorsConfiguration {
    //TODO Fix "Failed to load ApplicationContext...." the problem is @Inject
	//http://stackoverflow.com/questions/32580867/nosuchbeandefinitionexception-no-qualifying-bean-of-type-found?rq=1
	@Inject
	private StreetLampRepository streetLampRepository;
	private ArrayList<Street> street;

	public ArrayList<Street> getStreet() {
		return street;
	}

	public void setStreet(ArrayList<Street> street) {
		this.street = street;
	}
	
	public void readConfig() throws IOException{
		
		File file = new File("resources/Sensors");
		Scanner s = null;

		s = new Scanner(file);
		
		ArrayList<Street> list = new ArrayList<Street>();
		while (s.hasNext()){
			Street st = new Street();
			String [] line = s.nextLine().split("\\s+");
		    
			if(line[0].equals("")){
		    	continue;
		    }
		    if(line[0].startsWith("num_lamp:")){
        		st.setNumLamp(line[0].substring(9));
        	}else{
        		s.close();
		    	throw new InvalidObjectException(line[0]);
        	}
	        for(String i:line){
	        	if(i == line[0])
	        		continue;
	        	else if(i.startsWith("address:")){
	        		st.setAddress(i.substring(8).replace("_", " "));
	        	}
	        	else if(i.startsWith("model:")){
	        		st.setModel(i.substring(6));
	        	}
	        	else if(i.startsWith("consumption:")){
	        		st.setConsumption(i.substring(12));
	        	}
	        	else if(i.startsWith("intensity_mrn:")){
	        		st.setIntensityMrn(i.substring(15));
	        	}
	        	else if(i.startsWith("intensity_aft:")){ 
	        		st.setIntensityAft(i.substring(14));		
	        	}
			    else if(i.startsWith("intensity_evng:")){
			    	st.setIntensityEvng(i.substring(15));
	        	}
			    else if(i.startsWith("intensity_nght:")){
			    	st.setIntensityEvng(i.substring(15));
	        	}
	        }
	        list.add(st);
		}
		s.close();	
		this.setStreet(list);		
	}
	
	public void ConfigLamp() throws IOException, ParseException{

		readConfig();
    	int i = getStreet().size()-1;
    	int cont=1;
    	while(i>0){
    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    		String tmp = dateFormat.format(new Date());
    		Date timeStamp = null;
    		timeStamp = dateFormat.parse(tmp);
    		
    		int j=Integer.parseInt(getStreet().get(i).getNumLamp());
    		while(j>0){    		
	    		StreetLamp lp = new StreetLamp(); //si salva cosi?
	    		lp.setBulbModel(getStreet().get(i).getModel());
	    		lp.setId(""+cont+"");
	    		lp.setLigthIntensity("0");
	    		lp.setPosition(getStreet().get(i).getAddress()+" "+j+"");
	    		lp.setPowerConsumption("0");
	    		lp.setState("OFF");
	    		lp.setlastSubstitutionDate(""+timeStamp+"");
	    		lp.setStreet(getStreet().get(i));

	    		cont=cont+1;
	    		j=j-1;
    		}
            i=i-1;
    	}
    	
	}
	
	@Bean
	public void initThreadList() {
		
		try {
			ConfigLamp();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		List<StreetLamp> streetLampList = null;
		streetLampList = streetLampRepository.findAll();
		
		for (StreetLamp lamp : streetLampList){
			StreetLampThread sl = new StreetLampThread(lamp);					
			MappingThreadsLamps.getInstance().put(lamp.getId(), sl);
			
			sl.start();
		}
	}
	
}
