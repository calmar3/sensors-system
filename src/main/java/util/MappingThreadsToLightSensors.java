package util;

import java.util.HashMap;

import thread.LightSensorThread;

public class MappingThreadsToLightSensors {
	
	private static HashMap<Long, LightSensorThread> instance = null;
	
	protected MappingThreadsToLightSensors(){}
	
	public synchronized static final HashMap<Long, LightSensorThread> getInstance(){
		if(instance == null)
			instance = new HashMap<Long, LightSensorThread>();
		
		return instance;
	}
	
}