package configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;

import kafka.LigthSensorsProducer;
import model.LigthSensor;

public class LigthSensorThread extends Thread {
	
	private LigthSensor sensorLight;
	private LigthSensorsProducer producer;
	private boolean stop = false;
	
	
	public LigthSensorThread(LigthSensor sensorLight) {
		this.sensorLight = sensorLight;
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		
		producer = new LigthSensorsProducer();
	    producer.initialize();
		Double ligthIntensityMrn = ThreadLocalRandom.current().nextDouble(0.7, 1);
		Double ligthIntensityAft = ThreadLocalRandom.current().nextDouble(0.3, 0.7);
		Double ligthIntensityEvng = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
		Double ligthIntensityNght = ThreadLocalRandom.current().nextDouble(0.0, 0.2);
		
		
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
	    			this.sensorLight.setLigthIntensity(ligthIntensityMrn.toString());
	
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){ 			
					this.sensorLight.setLigthIntensity(ligthIntensityAft.toString());
	
	
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					this.sensorLight.setLigthIntensity(ligthIntensityEvng.toString());
	    			
	    			
	
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					this.sensorLight.setLigthIntensity( ligthIntensityNght.toString());

				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}			
	        //publish tuple on kafka topic
	    	JSONObject jo = null;
	        try {
				jo = LigthSensor.toJSONObject(this.sensorLight);
			} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
			} 
	        	
	        Date date = new Date();
	        jo.put("timestamp", date.getTime()); //add timestamp UTC 1/1/1970 epoch
        	producer.publish(this.sensorLight.getId(), jo.toString()); //Publish message to brokers
	    }
				
		if(stop){
            // Close the connection between broker and producer
            producer.closeProducer();

			Thread.currentThread().interrupt();
		}
	}

}