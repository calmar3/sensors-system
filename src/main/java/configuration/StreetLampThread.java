package configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kafka.StreetLigthSensorProducer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.StreetLamp;

public class StreetLampThread extends Thread {
	
	private StreetLamp streetLamp;
	private double ligthIntensityAdjustment = 1;
	private long sleepTime = 10;
	
	
	public StreetLampThread(StreetLamp streetLamp) {
		this.streetLamp = streetLamp;
	}
	
	public double getLigthIntensityAdjustment() {
		return ligthIntensityAdjustment;
	}

	public void setLigthIntensityAdjustment(double ligthIntensityAdjustment) {
		this.ligthIntensityAdjustment = ligthIntensityAdjustment;
	}	
	
	@Override
	public void run() {
		
		while(true) {
			try {
				Date date1 = new SimpleDateFormat("HH:mm").parse("24:00");
				Date date2 = new SimpleDateFormat("HH:mm").parse("06:00");
				Date date3 = new SimpleDateFormat("HH:mm").parse("12:00");
				Date date4 = new SimpleDateFormat("HH:mm").parse("18:00");
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	    		String tmp = dateFormat.format(new Date());
	    		Date timeStamp = null;
	    		timeStamp = dateFormat.parse(tmp);
	    		
	    		Double tmpLigthIntensity;
	    		
	    		if(date2.before(timeStamp) && date3.after(timeStamp)){
	    			tmpLigthIntensity = Double.parseDouble(this.streetLamp.getStreet().getIntensityMrn())*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){
					tmpLigthIntensity = Double.parseDouble(this.streetLamp.getStreet().getIntensityAft())*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(this.streetLamp.getStreet().getConsumption());
	    			this.streetLamp.setState("ON");
	
	
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					tmpLigthIntensity = Double.parseDouble(this.streetLamp.getStreet().getIntensityEvng())*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(this.streetLamp.getStreet().getConsumption());
	    			this.streetLamp.setState("ON");
	
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					tmpLigthIntensity = Double.parseDouble(this.streetLamp.getStreet().getIntensityNght())*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(this.streetLamp.getStreet().getConsumption());
	    			this.streetLamp.setState("ON");
				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}			
	        if(this.streetLamp.getState().equals("ON")){
	    		JSONObject jo = null;
	        	try {
					jo = StreetLamp.toJSONObject(this.streetLamp);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} 
	            
	        	//creare oggetto jeson e inviarlo con kafka 
	            
	            StreetLigthSensorProducer producer = new StreetLigthSensorProducer();
	            // Initialize the producer with required properties
	            producer.initialize();           
	            // Publish message to brokers
	            producer.publish(this.streetLamp.getId(), jo.toString());//
	            // Close the connection between broker and producer
	            producer.closeProducer();
	        	
	        }
			try {	
				Thread.sleep(sleepTime*1000);
			}
			catch(InterruptedException e) { //Eccezione sollevata nel caso il thread venga interrotto
					Thread.currentThread().interrupt();
			}	
		}
	}
}