package util;

import java.util.HashMap;

import configuration.LigthSensorThread;

public class MappingThreadsToLightSensors {
	
	private static HashMap<String, LigthSensorThread> instance = null;
	
	protected MappingThreadsToLightSensors(){}
	
	public synchronized static final HashMap<String, LigthSensorThread> getInstance(){
		
		if(instance == null)
			instance = new HashMap<String, LigthSensorThread>();
		
		return instance;
	}
	
}