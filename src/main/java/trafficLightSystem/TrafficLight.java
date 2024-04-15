/**
 * 
 */
package trafficLightSystem;


/**
 * 
 */
public class TrafficLight {

	// vars
	private static int nextSystemID = 804;
	private int trafficLightID;
	private int trafficLightSystemID;   // Traffic Light System id the VRS is associated to
	private String state;
	private String position;
	private boolean status;
	
	// default constructor
	public TrafficLight(int trafficLightSystemID) {
		//this.state = state;
		this.status = true;
		this.trafficLightID = ++nextSystemID;    // auto increment id
		this.trafficLightSystemID = trafficLightSystemID;   // Traffic Light System id the TL is associated to
		this.position = "";
	}
	
	
	public TrafficLight(String state) {

		this.state = state;
		this.status = true;
		this.trafficLightID = ++nextSystemID;    // auto increment id
	}



	// setters
	
	/**
	 * Set TL state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Set TL status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	

	/**
	 * Set TL position
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	// getters
	
	/**
	 * Get Traffic Light Id the TL is associated to
	 */
	public int getTrafficLightID() {
		return trafficLightID;
	}

	/**
	 * Get Traffic Light System Id the TL is associated to
	 */
	public int getTrafficLightSystemID() {
		return trafficLightID;
	}
	
	/**
	 * Get TL state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Get TL position
	 */
	public String getPosition() {
		
		return this.position;
	}

	/**
	 * Get TL status
	 */
	public boolean isStatus() {
		return status;
	}

}
