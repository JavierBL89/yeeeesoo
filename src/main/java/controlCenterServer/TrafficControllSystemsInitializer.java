package controlCenterServer;


import trafficControlSystem.TrafficControlSystem;


/***
 * Class responsible for the initialising all Traffic Control Systems managed within the Control Centre network. 
 * It ensures that each Traffic Control System is operational.
 *
 * ***/
public class TrafficControllSystemsInitializer {

	// objects
	private TCSystemsListManager listOfTrafficControlSystems = null;	
	
	// Cotructor
	public TrafficControllSystemsInitializer() {
		
    // get list of Traffic Control Systems associated to Control Centre
    listOfTrafficControlSystems = TCSystemsListManager.getInstance(); 

    }
	
	/**
	 * Method initialise all Traffic Control Systems within the  network. 
	 *  This method uses encapsulates the process of starting up the traffic control systems and 
	 * initiating the traffic control cycle with predefined initial states. 
	 * 
	 * It iterates through each system in the list, checks their operational status,
	 *  and initialises the Traffic Light Systems associated with each  Traffic Control System
	 *  or prints and error message is the are not operational
	 *  
	 * The initialization process involves two primary actions:
	 * 1. Initializing traffic control systems: Ensuring that all systems are set up and configured.
	 *    
	 * 2. Starting the traffic control cycle: Kicking off the cycle that governs traffic light changes,
	 *    starting with predefined initial states to then be modified by traffic density changes.
	 *
	 * @throws Exception 
	 * **/
	public  void initTrafficControlSystems() throws Exception {
		
		System.out.println("1- Initialiting Traffic Controll System..."); 
		
		// Iterate through each Traffic Control System in the list
		for(TrafficControlSystem tcs : listOfTrafficControlSystems) {
			
			if(tcs.isOperative()) {     // Check if the Traffic Control System is operative
				// confirm it has been been successfully initialized
				System.out.println("\nTraffic Control System " + tcs.getSystemID() + " is up and running.");
				System.out.println("\n--------"); 
				
				 
				tcs.initTrafficLightSystems(); // Initialise the Traffic Light Systems associated to the Traffic Control System
			}else {
				
				 // Print an error message if Traffic Control System is operative
				System.out.println("Error at initializing Traffic Control System with id " + tcs.getSystemID()
				+ ". This system is not operative");
			}
		}
		System.out.println(" ");
	}
	
	
	/***
	 * Method to initialize proccess of Traffic Control.
	 * 
	 * Iterates over the list of associated Traffic Control Systems
	 * and request to start the cycle with apredifined initial state.
	 */
	public void startTrafficControlCycle() {
		
		for(TrafficControlSystem tcs : listOfTrafficControlSystems) {
			tcs.startTrafficControlCycle("green");
		}
	}
	
}

