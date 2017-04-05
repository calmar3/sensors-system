package configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;

import kafka.StreetLampSensorProducer;
import model.StreetLamp;

public class StreetLampThread extends Thread {
	
	private StreetLamp streetLamp;
	private StreetLampSensorProducer producer;
	private double ligthIntensityAdjustment = 1;
	private boolean stop = false;
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
		Double tmpLigthIntensity;
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
	    			tmpLigthIntensity = intensityMrn*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){
					tmpLigthIntensity = intensityAft*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(consumption.toString());
	    			this.streetLamp.setState("ON");
	
	
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					tmpLigthIntensity = intensityEvng*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(consumption.toString());
	    			this.streetLamp.setState("ON");
	
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					tmpLigthIntensity = intensityNght*ligthIntensityAdjustment;
	    			this.streetLamp.setLigthIntensity(tmpLigthIntensity.toString());
	    			
	    			this.streetLamp.setPowerConsumption(consumption.toString());
	    			this.streetLamp.setState("ON");
				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}			
	        if(this.streetLamp.getState().equals("ON")){//publish tuple on kafka topic
	    		JSONObject jo = null;
	        	try {
					jo = StreetLamp.toJSONObject(this.streetLamp);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} 
	        	
	        	Date date = new Date();
	        	jo.put("timestamp", date.getTime()); //add timestamp UTC 1/1/1970 epoch

	        	producer.publish(this.streetLamp.getId(), jo.toString()); //Publish message to brokers
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