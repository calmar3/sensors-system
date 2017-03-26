package configuration;

import java.util.HashMap;

public class MappingThreadsLamps {
	
	private static HashMap<String, StreetLampThread> instance = null;
	
	protected MappingThreadsLamps(){}
	
	public synchronized static final HashMap<String, StreetLampThread> getInstance(){
		
		if(instance == null)
			instance = new HashMap<String, StreetLampThread>();
		
		return instance;
	}
	
}