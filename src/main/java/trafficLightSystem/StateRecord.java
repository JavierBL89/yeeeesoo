/**
 * Javier Bastande
 */
package trafficLightSystem;

/**
 * Class Represents the past state of Traffic Light Systems.
 * It stores the state and system ID of a traffic light system at a specific time,
 * allowing tracking of state changes over time for comparison and data analysing purposes.
 */
public class StateRecord {

	// vars
	private static int tlsID;
	private static String state;
	private static StateRecord instance;
	
	
	//constructor
	public StateRecord(int tlsID, String newState){
		this.tlsID = tlsID;
		this.state = newState;

	}
	
	
	// getters

	/**
	 * @return the tlsID
	 */
	public int getTLSID() {
		return tlsID;
	}

	/**
	 * 
	 * Get state
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	
	// setters
	
	/**
	 * @param tlsID the tlsID to set
	 */
	public void setTLSID(int tlsID) {
		StateRecord.tlsID = tlsID;
	}

	/**
	 * Set state
	 * 
	 * @param state the state to set
	 */
	public void setState(String state) {
		StateRecord.state = state;
	}
	
	
}
