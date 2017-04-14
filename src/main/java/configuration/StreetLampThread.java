package configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import kafka.StreetLampSensorProducer;
import model.StreetLamp;

import org.json.simple.JSONObject;

public class StreetLampThread extends Thread {
	
	private StreetLamp streetLamp;
	private StreetLampSensorProducer producer;
	private double lightIntensityAdjustment = 1;
	private boolean stop = false;
	private long sleepTime = 10;
	
	
	public StreetLampThread(StreetLamp streetLamp) {
		this.streetLamp = streetLamp;
	}
	
	public double getLightIntensityAdjustment() {
		return lightIntensityAdjustment;
	}

	public void setLightIntensityAdjustment(double lightIntensityAdjustment) {
		this.lightIntensityAdjustment = lightIntensityAdjustment;
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		
		producer = new StreetLampSensorProducer();
	    producer.initialize();
	    
		Double tmpLightIntensity;
		Double intensityMrn = 0.0;
		Double intensityAft = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
		Double intensityEvng = ThreadLocalRandom.current().nextDouble(0.6, 1);
		Double intensityNght = ThreadLocalRandom.current().nextDouble(0.5, 0.7);
		Double consumption = ThreadLocalRandom.current().nextDouble(50, 60);
		
		
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
	    			tmpLightIntensity = intensityMrn+lightIntensityAdjustment;
	    			this.streetLamp.setLightIntensity(tmpLightIntensity);
	
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){
					tmpLightIntensity = intensityAft+lightIntensityAdjustment;
	    			this.streetLamp.setLightIntensity(tmpLightIntensity);
	    			
	    			this.streetLamp.setConsumption(consumption);
	    			this.streetLamp.setStateOn(true);
	
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					tmpLightIntensity = intensityEvng+lightIntensityAdjustment;
	    			this.streetLamp.setLightIntensity(tmpLightIntensity);
	    			
	    			this.streetLamp.setConsumption(consumption);
	    			this.streetLamp.setStateOn(true);
	
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					tmpLightIntensity = intensityNght+lightIntensityAdjustment;
	    			this.streetLamp.setLightIntensity(tmpLightIntensity);
	    			
	    			this.streetLamp.setConsumption(consumption);
	    			this.streetLamp.setStateOn(true);
				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			Date date = new Date();
        	this.streetLamp.setTimestamp(date.getTime());//add timestamp UTC 1/1/1970 epoch
        	
	        if(this.streetLamp.isStateOn()){//publish tuple on kafka topic
	    		JSONObject jo = null;
	    		
	        	try {
					jo = StreetLamp.toJSONObject(this.streetLamp);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} 
	        	
	        	producer.publish(String.valueOf(this.streetLamp.getLampId()), jo.toString()); //Publish message to brokers
	        }
			try {	
				Thread.sleep(sleepTime*1000);
			}
			catch(InterruptedException e) {
				producer.closeProducer();
				Thread.currentThread().interrupt();
			}	
		}
		
		if(stop){
            // Close the connection between broker and producer
            producer.closeProducer();

			Thread.currentThread().interrupt();
		}
	}

}