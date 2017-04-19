package util;

import java.util.HashMap;

import thread.StreetLampThread;

public class MappingThreadsToLamps {
	
	private static HashMap<Long, StreetLampThread> instance = null;
	
	protected MappingThreadsToLamps(){}
	
	public synchronized static final HashMap<Long, StreetLampThread> getInstance(){
		if(instance == null)
			instance = new HashMap<Long, StreetLampThread>();
		
		return instance;
	}
	
}