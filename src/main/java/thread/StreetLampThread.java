package thread;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import model.StreetLamp;

import org.json.simple.JSONObject;

import util.MappingAdjustmentToLamps;
import configuration.KafkaProducer;

public class StreetLampThread extends Thread {
	
	private String STREETLAMP_TOPIC;
	private long STREETLAMP_THREAD_SLEEPTIME;
	
	private List<StreetLamp> streetLampList;
	private MappingAdjustmentToLamps listAdjustment;
	private boolean stop;
	private int warning;
	
	public StreetLampThread(){
		this.streetLampList = new ArrayList<StreetLamp>();
		this.listAdjustment = new MappingAdjustmentToLamps();
		this.stop = false;
		this.warning = 0;
	}
	
	public void setTopic(String topic){
		this.STREETLAMP_TOPIC = topic;
	}
	
	public void setSleepTime(long sleepTime){
		this.STREETLAMP_THREAD_SLEEPTIME = sleepTime;
	}
	
	public List<StreetLamp> getStreetLampList() {
		return streetLampList;
	}

	public void setStreetLampList(List<StreetLamp> streetLampList) {
		this.streetLampList = streetLampList;
	}
	
	public MappingAdjustmentToLamps getListAdjustment() {
		return listAdjustment;
	}

	public void setListAdjustment(MappingAdjustmentToLamps listAdjustment) {
		this.listAdjustment = listAdjustment;
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		
		Double intensityMrn = 0.0;
		Double intensityAft = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
		Double intensityEvng = ThreadLocalRandom.current().nextDouble(0.6, 1);
		Double intensityNght = ThreadLocalRandom.current().nextDouble(0.5, 0.7);
		Double consumption = ThreadLocalRandom.current().nextDouble(55, 60);
		
		Double tmpLightIntensity = 0.0;
		Double tmpConsumption = 0.0;
		boolean tmpStateOn = false;
		
		while(!stop) {
			try {
				Date date1 = new SimpleDateFormat("HH:mm").parse("24:00");
				Date date2 = new SimpleDateFormat("HH:mm").parse("06:00");
				Date date3 = new SimpleDateFormat("HH:mm").parse("12:00");
				Date date4 = new SimpleDateFormat("HH:mm").parse("18:00");
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	    		String tmp = dateFormat.format(new Date());
	    		Date timeStamp = null;
	    		timeStamp = dateFormat.parse(tmp);
	    		
	    		if(date2.before(timeStamp) && date3.after(timeStamp)){
	    			tmpLightIntensity = intensityMrn;
	    			tmpConsumption = consumption;
	    			tmpStateOn = true;
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){
					tmpLightIntensity = intensityAft;
					tmpConsumption = consumption;
	    			tmpStateOn = true;
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					tmpLightIntensity = intensityEvng;
					tmpConsumption = consumption;
	    			tmpStateOn = true;
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					tmpLightIntensity = intensityNght;
					tmpConsumption = consumption;
	    			tmpStateOn = true;
				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
        	for(StreetLamp sl: streetLampList){
        		sl.setStateOn(tmpStateOn);
        		if(sl.isStateOn() && !stop){
        			if(sl.getLampId() == 10 || sl.getLampId() == 40){//Continuously generate 2 warning
        				sl.setStateOn(false);
	                }
        			
	        		Date date = new Date();
	            	sl.setTimestamp(date.getTime());//add timestamp UTC 1/1/1970 epoch
	            	sl.setResidualLifeTime(sl.getTimestamp() - sl.getLastSubstitutionDate());//add timestamp UTC 1/1/1970 epoch
	        		
	            	BigDecimal bg = new BigDecimal(tmpLightIntensity+this.listAdjustment.getMappingAdjustmentToLamps().get(sl.getLampId())); 
	        		bg = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
	        		double intensity = bg.doubleValue();
	            	
	        		sl.setLightIntensity(intensity);
	        		
	                String model = sl.getModel().substring(0, 1);
	                switch (model) {//Consumption value changes based on lamp model
	                    case "A":
	                    	tmpConsumption = tmpConsumption - 1.5;//LED
	                        break;
	                    case "B":
	                    	tmpConsumption = tmpConsumption - 1.25;
	                        break;
	                    case "C":
	                    	tmpConsumption = tmpConsumption - 1;
	                        break;
	                    case "D":
	                    	tmpConsumption = tmpConsumption - 0.75;
	                        break;
	                    case "E":
	                    	tmpConsumption = tmpConsumption - 0.5;
	                        break;
	                    case "F":
	                    	tmpConsumption = tmpConsumption - 0.25;
	                        break;
	                    default: break;
	                }
	                if(warning == 5 && (sl.getLampId() == 30 || sl.getLampId() == 50)){//Each 5min generate a 2 warning
	                	tmpConsumption = tmpConsumption*3;
	                	warning = 0;
	                }
        		
	        		sl.setConsumption(tmpConsumption);
	        		
	        		JSONObject jo = null;
                	try {
        				jo = StreetLamp.toJSONObject(sl);
        			} catch (IllegalArgumentException | IllegalAccessException e) {
        				e.printStackTrace();
        			}
	        		//System.out.println("StreetLamp"+Thread.currentThread().getName()+" lamp id "+sl.getLampId()+" lightIntesity "+sl.getLightIntensity()+"\n");
                	KafkaProducer.send(STREETLAMP_TOPIC, jo.toString());
        		}
        	}
        	
        	warning++;
        	
			try {	
				Thread.sleep(STREETLAMP_THREAD_SLEEPTIME);
			}
			catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}	
		}
		
		if(stop){
			Thread.currentThread().interrupt();
		}
	}

}