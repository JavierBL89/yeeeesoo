/**
 * 
 */
package trafficLightSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import visualRecognitionSystem.VisualRecognitionSystem;


/**
 * 
 */
public class TrafficLightSystem {
	
	// vars
	private static int nextSystemID = 304;
	private int systemID;
	private String state;
	private List<TrafficLight> trafficLights;
	private List<VisualRecognitionSystem> visualRecognitionSystems;
	
	private TrafficLight tlA ;
	private TrafficLight tlB ;
	private VisualRecognitionSystem vrsA;
	private VisualRecognitionSystem vrsB;
	private boolean operative;

	

	/***
	 * Constructor to initialise a new Object Traffic Light System
	 * and the pair of traffic lights that compose this system
	 * ***/
	public TrafficLightSystem() {
		this.systemID = ++nextSystemID;   // auto increment id
		this.operative = true;
		this.state = "";
		this.trafficLights = new ArrayList<>();
		this.visualRecognitionSystems = new ArrayList<>();
	}

	/**
	 * Method initialises the components of a Traffic Light System (TLS),
     * including Traffic Lights (TL) and Visual Recognition Systems (VRS).
	 * 
	 * Synchronous components initialitation.
	 * 
	 * This method ensures that every operational traffic light 
	 * has a corresponding VRS(camera) associated with it through their id. 
	 * 
	 * Exception Handling:
     * The method includes error handling to manage any issues that may arise during the initialization 
     * of Traffic Lights and Visual Recognition Systems. In the event of a failure, a detailed exception is thrown,
     * indicating what went wrong during the process.
     */
	public void initTLSComponents() throws Exception {
		
		Thread.sleep(500);
		System.out.println("\n***Initialiting Traffic Light System " + this.getSystemId() + "***");
		Thread.sleep(500);
		System.out.println("\nInit Traffic Lights");
		
		try {
			// Init 2 traffic lights
			tlA = new TrafficLight(this.systemID);
			tlB = new TrafficLight(this.systemID);
			
			
			System.out.println("Traffic Light with id " + tlA.getTrafficLightID() + " " + tlA.getPosition() +
	    	    	   " is up and running");
			System.out.println("Traffic Light with id " + tlB.getTrafficLightID() + " " + tlB.getPosition() +
	    	    	   " is up and running");
			
			Thread.sleep(500);
		    System.out.println("\nInit Visual Recognition Systems");
			Thread.sleep(500);
			
			// Init 2 visual recognition systems associated to each traffic light
			vrsA = new VisualRecognitionSystem(tlA.getTrafficLightID(), this.systemID);
		    vrsB = new VisualRecognitionSystem(tlB.getTrafficLightID(), this.systemID);
			
			System.out.println("Visual Recognition System with id " + vrsA.getTrafficLightID() + " " + tlA.getPosition() +
	    	    	   " associated with Traffic Light " + tlA.getTrafficLightID() + " is up and running");
			
			System.out.println("Visual Recognition System with id " + vrsB.getTrafficLightID() + " " + tlB.getPosition() +
	    	    	   " associated with Traffic Light " + tlB.getTrafficLightID() + " is up and running");
		    
			System.out.println("\nTraffic Light System with id " + this.getSystemId() + " has been successfully initialized.");
			System.out.println("\n------------------------------------------------------------------------------------------");
			
			// Add the TL and VRS to their respective lists, this lists will allow scalability and better accessibility
	        this.trafficLights.add(tlA);
	        this.trafficLights.add(tlB);
	        this.visualRecognitionSystems.add(vrsA);
	        this.visualRecognitionSystems.add(vrsB);
	        
		}catch(Exception e) {
			throw new Exception("Somethig went wrong and System components could not be initialized: " 
		                + "\nInitialization error: " + e.getMessage() );
		}
		
	}
	
	
	// getters
		
	/**
	* Get Traffic Light System status
	* */	
	public int getSystemId() {
		return systemID;
	}
	
	/**
	 * Get Traffic Light System status
	 * */
	public boolean isOperative() {
		return operative;
	}
	
	/**
	 * Get Traffic Light A
	 * */
	public TrafficLight getTlA() {
		return tlA;
	}

	/**
	 * Get Traffic Light B
	 * */
	public TrafficLight getTlB() {
		return tlB;
	}
	
	/**
	 * Get Traffic Light System state
	 * */
	public String getState() {
		return this.state;
	}

	/**
	 * Get list of all Traffic Lights associated to this system
	 * 
	 * @return trafficLights  list
	 * **/
	public List<TrafficLight> getTrafficLights(){
		
		return trafficLights;
	}
	
	
	/**
	 * Get list of all Visual Recognition Systems associated to this system
	 * 
	 * @return visualRecognitionSystems  list
	 * **/
   public List<VisualRecognitionSystem> getVisualRecognitionSystems(){
		
		return visualRecognitionSystems;
	}
   
   
	// helper methods
	
	
	/***
	 * Method adds a new traffic light to the list of associated TL to this system
	 * **/
	public void addTrafficLight(TrafficLight tl) {

		this.trafficLights.add(tl);
	}
	
	
	/***
	 * Method adds a new visual recognition system to the list of associated VRS to this system
	 * **/
	public void addVisualRecognitionSystem(VisualRecognitionSystem vrs) {

		this.visualRecognitionSystems.add(vrs);
	}
	
	/***
	 * Method updates the state of both traffic lights of the system
	 * */
		public void updateLightsState(String newState) {
			this.state = newState;          // update Traffic Light System state 
			
			this.tlA.setState(newState);    // update state traffic Light A
			this.tlB.setState(newState);    // update state traffic Light B
		};
		

	/***
    * Method to start traffic data collection cycle
    * **/
	public void startVRDataCollection() {
		
		// Create a thread pool with as many threads as there are VRS instances
        ExecutorService executor = Executors.newFixedThreadPool(visualRecognitionSystems.size());
        
		    // Iterates over the list of Visual Recognition Systems associated to this Traffic Light System
			for(VisualRecognitionSystem vrs : this.visualRecognitionSystems) {
				 executor.submit(() -> {
		                vrs.startDataCollectorCycle();
		                System.out.println("Data collection finished...");
		            });

		        System.out.println("Data collection started...");
			}
			
			// Shutdown the executor after starting all tasks
	        executor.shutdown();
	}
	
	
	/***
	* Method to print the last data collection report
	* **/
	public void reportDataCollection() {
		
		// Iterates over the list of Visual Recognition Systems associated to this Traffic Light System
		for(VisualRecognitionSystem vrs : visualRecognitionSystems) {
					vrs.printDetailedScanReport();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TrafficLightSystem p = new TrafficLightSystem();
		
	}

}
