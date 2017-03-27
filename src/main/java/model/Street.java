package model;

public class Street {

	private String numLamp;
	private String address;
	private String model;
	private String consumption;
	private String intensityMrn;
	private String intensityAft;
	private String intensityEvng;
	private String intensityNght;

	
	public Street(){
		this.numLamp = null;
		this.address = null;
		this.model = null;
		this.consumption = null;
		this.intensityMrn = null;
		this.intensityAft = null;
		this.intensityEvng = null;
		this.intensityNght = null;	
	}
	
	public Street(String numLamp, String address, String model, String consumption, 
			      String intensityMrn, String intensityAft, String intensityEvng,  String intensityNght){
		this.numLamp = numLamp;
		this.address = address;
		this.model = model;
		this.consumption = consumption;
		this.intensityMrn = intensityMrn;
		this.intensityAft = intensityAft;
		this.intensityEvng = intensityEvng;
		this.intensityNght = intensityNght;	
	}

	public String getNumLamp() {
		return numLamp;
	}

	public void setNumLamp(String numLamp) {
		this.numLamp = numLamp;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}

	public String getIntensityMrn() {
		return intensityMrn;
	}

	public void setIntensityMrn(String intensityMrn) {
		this.intensityMrn = intensityMrn;
	}
	
	public String getIntensityAft() {
		return intensityAft;
	}

	public void setIntensityAft(String intensityAft) {
		this.intensityAft = intensityAft;
	}
	
	public String getIntensityEvng() {
		return intensityEvng;
	}

	public void setIntensityEvng(String intensityEvng) {
		this.intensityEvng = intensityEvng;
	}
	
	public String getIntensityNght() {
		return intensityNght;
	}

	public void setIntensityNght(String intensityNght) {
		this.intensityNght = intensityNght;
	}

}
