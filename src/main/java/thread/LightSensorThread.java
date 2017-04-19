package thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import model.LightSensor;

import org.json.simple.JSONObject;

import configuration.KafkaProducer;

public class LightSensorThread extends Thread {
	
	private static final String TOPIC = "light_sensor_data";
	
	private List<LightSensor> lightSensorList;
	private boolean stop;
	
	public LightSensorThread(){
		this.lightSensorList = new ArrayList<LightSensor>();
		this.stop = false;
	}
	
	public List<LightSensor> getLightSensorList() {
		return lightSensorList;
	}

	public void setLightSensorList(List<LightSensor> lightSensorList) {
		this.lightSensorList = lightSensorList;
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		
		Double lightIntensityMrn = ThreadLocalRandom.current().nextDouble(0.7, 1);
		Double lightIntensityAft = ThreadLocalRandom.current().nextDouble(0.3, 0.7);
		Double lightIntensityEvng = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
		Double lightIntensityNght = ThreadLocalRandom.current().nextDouble(0.0, 0.2);
		
		Double tmpLightIntensity = 0.0;
		
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
	    			tmpLightIntensity = lightIntensityMrn;
				}
				else if(date3.before(timeStamp) && date4.after(timeStamp)){ 			
					tmpLightIntensity = lightIntensityAft;
				}
				else if(date4.before(timeStamp) && date1.after(timeStamp)){
					tmpLightIntensity = lightIntensityEvng;
				}
				else if(date1.before(timeStamp) && date2.after(timeStamp)){
					tmpLightIntensity = lightIntensityNght;
				}
	    		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}	
			
	        //publish tuple on kafka topic
        	for(LightSensor ls: lightSensorList){
        		Date date = new Date();
            	ls.setTimestamp(date.getTime());//add timestamp UTC 1/1/1970 epoch
        		ls.setLightIntensity(tmpLightIntensity);
        		
        		JSONObject jo = null;
            	try {
    				jo = LightSensor.toJSONObject(ls);
    			} catch (IllegalArgumentException | IllegalAccessException e) {
    				e.printStackTrace();
    			}
            	//System.out.println("LightSensor"+Thread.currentThread().getName()+" lamp id "+ls.getLightSensorId());
            	KafkaProducer.send(TOPIC, jo.toString());
        	}
		
		}
		if(stop){
			Thread.currentThread().interrupt();
		}
	}

}