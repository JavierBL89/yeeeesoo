/**
 * Javier Bastande
 */
package controlCenterServer;

import trafficControlSystem.TrafficControlSystem;

/**
 * Class ats as Control Center of the Traffic Light Management System.
 * It is the starting point of system.
 * It manages the initialization and operation of multiple Traffic Control Systems within a control center. 
 * 
 * This class is responsible for setting up traffic control systems, configuring visual recognition systems,
 * and starting the traffic control cycle with predefined initial states.
 * 
 * 
 */
public class ControlCenterServer {
	
	// vars
	private int systemID = 700;
	private static TrafficControllSystemsInitializer tcsInitializer;
	
	/** Default constructor */
	public ControlCenterServer() {
		this.systemID++;
		this.tcsInitializer = new TrafficControllSystemsInitializer();
	}
	

	/**
	 * Get system id
	 * 
	 * @return the systemID
	 */
	public int getSystemID() {
		return systemID;
	}

	// helper methods
	
	 /*
	 * Method to initialize all Traffic Control Systems managed by the Traffic Control System.
	 * It uses Traffic Control System Initializer to start the process.
	 * 
	 * The initialization process involves two primary actions:
	 * 1. Initializing traffic control systems: Ensuring that all systems are set up and configured.
	 *    
	 * 2. Starting the traffic control cycle: Kicking off the cycle that governs traffic light changes,
	 *    starting with predefined initial states to then be modified by traffic density changes.
	 */
	private static void initializeTrafficControlSystems() {
		
		// Init Traffic Controll Systems Initializer class
	    try {
	    	    tcsInitializer.initTrafficControlSystems();  // call static method of TrafficControllSystemsInitializer class
				
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * Mthod to configure the visual recognition parameters for all associated Visual Recognition Systems 
	 * associated with the Traffic Control System that this Control Centre manages
	 * 
	 * * The configuration option include:
	 * - The number of traffics scans withing a whole scan cycle.
	 * - The length of each of those traffic scans.
	 * 
	 * @param numOfScanCycles
	 * @param numOfScanCycles
	 * **/
	private static void configureVisualRecognitionSystem(int numOfScanCycles, int scanLenghtInSeconds) {
		
	    TCSystemsListManager listManager = TCSystemsListManager.getInstance();   // get instance of associated Traffic Control Systems list
	    
	    // iterate through list
	    for(TrafficControlSystem tcs : listManager) {
	       	tcs.configAllVisualRecognitionSystems(numOfScanCycles, scanLenghtInSeconds); // Configure visual recognition parameters
	    }
	}
	
	/***
	 * Method adds a new Traffic Control System to the list of
	 * Traffic Control Systems this Control Centre manages.
	 * 
	 * Thos method allow dinamci integration of new Traffic Control Systems which allows 
	 * adding and removing Traffic Control Systems for maintenance.
	 * 
	 * **/
	private static void addTrafficControlSystem() {
		
		TCSystemsListManager instance = TCSystemsListManager.getInstance(); // get instance of associated Traffic Control Systems list
		instance.addTrafficContolSystem(new TrafficControlSystem());        // add new Traffic Control System
		
	}
	
	
	/*
    * Method start the the process of Traffic Control cycles.
    * It uses the Traffic Control Initializer class functionalities to trigger the process.
    * 
    * Traffic Light Systems will start to collect and analize traffic data 
    * to manage the traffic lights state based on that data.
	*/
	private static void startTrafficControlCycle() {
			
		// Init Traffic Control Systems Initializer class
		try {
			tcsInitializer.startTrafficControlCycle();    // init the Traffic Control Cycle with predefined initial states
					
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
		

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ControlCenterServer n = new ControlCenterServer();
		
		addTrafficControlSystem();
		initializeTrafficControlSystems();
		configureVisualRecognitionSystem(/*numOfScans*/  3, /*scanLengthInaNoSeconds*/  2);    //     
	
		startTrafficControlCycle();
	}

	
}
