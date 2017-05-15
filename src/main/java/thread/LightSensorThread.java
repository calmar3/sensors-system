package thread;

import java.math.BigDecimal;
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
	
	private String LIGHT_SENSOR_TOPIC;
	private long LIGHTSENSOR_THREAD_SLEEPTIME;
	
	private List<LightSensor> lightSensorList;
	private boolean stop;
	
	public LightSensorThread(){
		this.lightSensorList = new ArrayList<LightSensor>();
		this.stop = false;
	}
	
	public void setTopic(String topic){
		this.LIGHT_SENSOR_TOPIC = topic;
	}
	
	public void setSleepTime(long sleepTime){
		this.LIGHTSENSOR_THREAD_SLEEPTIME = sleepTime;
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
			List<LightSensor> tmpLightSensorList = this.lightSensorList;
        	for(LightSensor ls: tmpLightSensorList){
        		Date date = new Date();
            	ls.setTimestamp(date.getTime());//add timestamp UTC 1/1/1970 epoch
            	
            	BigDecimal bg = new BigDecimal(tmpLightIntensity); 
        		bg = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
        		double intensity = bg.doubleValue();
            	
        		ls.setLightIntensity(intensity);
        		
        		JSONObject jo = null;
            	try {
    				jo = LightSensor.toJSONObject(ls);
    			} catch (IllegalArgumentException | IllegalAccessException e) {
    				e.printStackTrace();
    			}
            	//System.out.println("LightSensor"+Thread.currentThread().getName()+" lamp id "+ls.getLightSensorId()+" lightIntensity "+intensity+"\n");
            	KafkaProducer.send(LIGHT_SENSOR_TOPIC, jo.toString());
        	}
        	
        	try {	
				Thread.sleep(LIGHTSENSOR_THREAD_SLEEPTIME);
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