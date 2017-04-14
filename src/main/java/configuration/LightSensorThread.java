package configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;

import kafka.LightSensorsProducer;
import model.LightSensor;

public class LightSensorThread extends Thread {
	
	private LightSensor lightSensor;
	private LightSensorsProducer producer;
	private boolean stop = false;
	
	
	public LightSensorThread(LightSensor sensorLight) {
		this.lightSensor = sensorLight;
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		
		producer = new LightSensorsProducer();
	    producer.initialize();
		Double lightIntensityMrn = ThreadLocalRandom.current().nextDouble(0.7, 1);
		Double lightIntensityAft = ThreadLocalRandom.current().nextDouble(0.3, 0.7);
		Double lightIntensityEvng = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
		Double lightIntensityNght = ThreadLocalRandom.current().nextDouble(0.0, 0.2);
		
		
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
	    			this.lightSensor.setLightIntensity(lightIntensityMrn);
	
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){ 			
					this.lightSensor.setLightIntensity(lightIntensityAft);
	
	
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					this.lightSensor.setLightIntensity(lightIntensityEvng);
	    			
	    			
	
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					this.lightSensor.setLightIntensity( lightIntensityNght);

				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}	
			
			Date date = new Date();
        	this.lightSensor.setTimestamp(date.getTime());//add timestamp UTC 1/1/1970 epoch
			
	        //publish tuple on kafka topic
	    	JSONObject jo = null;
	        try {
				jo = LightSensor.toJSONObject(this.lightSensor);
			} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
			} 
	        	
        	producer.publish(String.valueOf(this.lightSensor.getLightSensorId()), jo.toString()); //Publish message to brokers
	    }
				
		if(stop){
            // Close the connection between broker and producer
            producer.closeProducer();

			Thread.currentThread().interrupt();
		}
	}

}