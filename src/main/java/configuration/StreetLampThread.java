package configuration;

import model.StreetLamp;

public class StreetLampThread extends Thread {
	
	private StreetLamp streetLamp;
	private long sleepTime = 10;
	
	
	public StreetLampThread(StreetLamp streetLamp) {
		this.streetLamp = streetLamp;
	}
	
	@Override
	public void run() {
		
		while(true) {
		
			//produce new json tuple to Kafka topic 			
			
			try {	
				Thread.sleep(sleepTime*1000);
			}
			catch(InterruptedException e) { //Eccezione sollevata nel caso il thread venga interrotto
					Thread.currentThread().interrupt();
			}	
		}
	}	
}