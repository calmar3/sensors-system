package util;

import java.util.HashMap;

import configuration.StreetLampThread;

public class MappingThreadsToLamps {
	
	private static HashMap<String, StreetLampThread> instance = null;
	
	protected MappingThreadsToLamps(){}
	
	public synchronized static final HashMap<String, StreetLampThread> getInstance(){
		
		if(instance == null)
			instance = new HashMap<String, StreetLampThread>();
		
		return instance;
	}
	
}