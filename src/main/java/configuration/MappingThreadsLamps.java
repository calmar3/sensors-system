package configuration;

import java.util.HashMap;

public class MappingThreadsLamps {
	
	private static HashMap<String, Object> tlmap = null;
	
	protected MappingThreadsLamps(){}
	
	public static HashMap<String, Object> getInstance(){
		
		if(tlmap == null)
			return new HashMap<String, Object>();
		
		return tlmap;
	}
	
}
