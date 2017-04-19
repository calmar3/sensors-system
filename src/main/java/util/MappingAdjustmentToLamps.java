package util;

import java.util.HashMap;

public class MappingAdjustmentToLamps {
	
	private HashMap<Long, Double> mappingAdjustmentToLamps = null;
	
	public MappingAdjustmentToLamps(){
		this.setMappingAdjustmentToLamps(new HashMap<Long, Double>());
	}

	public HashMap<Long, Double> getMappingAdjustmentToLamps() {
		return mappingAdjustmentToLamps;
	}

	public void setMappingAdjustmentToLamps(HashMap<Long, Double> mappingAdjustmentToLamps) {
		this.mappingAdjustmentToLamps = mappingAdjustmentToLamps;
	}

}