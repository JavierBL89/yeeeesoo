/**
 * Javie Bastande
 */
package trafficControlSystem;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import controlCenterServer.TCSystemsListManager;
import trafficLightSystem.StateRecord;
import trafficLightSystem.TrafficLight;
import trafficLightSystem.TrafficLightSystem;
import visualRecognitionSystem.TrafficDataCollector;
import visualRecognitionSystem.VisualRecognitionSystem;

/**
 * The TrafficControlSystem class manages its associated Traffic Light Systems, 
 * It orchestrates their operational cycles based on visual recognition data collected to optimize traffic flow.
 * 
 * This class is responsible for:
 * - Initializing Traffic Light Systems
 * - Configure their visual recognition systems
 * - Executing traffic control cycles to ensure efficient traffic management.
 */
public class TrafficControlSystem {
	
	    // vars
		private int systemID = 2012;
		private TrafficLightSystem tls1;
		private TrafficLightSystem tls2;
		private boolean isOperative;
		private int numOfVisualRecognitionScans;
		private int lengthOfVRScans;     // in nanoseconds
		private int trafficCycleLoops;
		private int cycleCount;
		private int maxCycles;
		// list holds the traffic light systems that are controlled by this Traffic Control System
		private List<TrafficLightSystem> listOfTrafficLightSystems;  
		
		// list holds the history of the Traffic Light Sytems with a "green" state
		private List<StateRecord> tlsStateHistory;
			   
	    
		/**
		 * Constructor to initialise Traffic Control System object
		 * **/
		public TrafficControlSystem() {
			this.isOperative = true;
			this.listOfTrafficLightSystems = new ArrayList<>();
			this.tlsStateHistory = new ArrayList<>();
			this.trafficCycleLoops = 0;
			this.cycleCount = 0;
			this.maxCycles = 3;
			//this.initTrafficLightSystems();  // call method to integrate the Traffic Light Systems
	        
		}
		
		
		/***
		* Method initialise the 2 Traffic Light Systems associated to 
		* this Traffic Control System.
		* 
		* Synchronous TLSs initialitation process.
		* 
	    * @throws Exception if a TLS is not operative or if there's an issue initializing its components. 
	    * 
		* **/
		public void initTrafficLightSystems() throws Exception {
			
			System.out.println("\n2- Initializing Traffic Light Systems...");
             
			this.initializeTLS1();   // init Traffic Light System 1
			this.initializeTLS2();   // init Traffic Light System 2

		}

		
		/***
		 * Method initialises Traffic System 1
		 * ***/
		public void initializeTLS1() {
			
			try {
			    // Initialize the first Traffic Light System and its components
			    tls1 = new TrafficLightSystem();
			    if (!tls1.isOperative()) {
			         throw new Exception("Traffic Light System 1 is not operative and could not be initialized.");
			    }
			    
			    listOfTrafficLightSystems.add(tls1); // Add TLS 1 to the list
			    tls1.initTLSComponents(); // Initialise associated components
			    
			    } catch (Exception e) {
			        System.err.println("Error initializing Traffic Light System 1: " + e.getMessage());
			       
			  }
		}
		
		/***
		 * Method initialises Traffic System 1
		 * ***/
		public void initializeTLS2() {
			try {
		        // Initialise the second Traffic Light System and its components
		        tls2 = new TrafficLightSystem();
		        if (!tls2.isOperative()) {
		            throw new Exception("Traffic Light System 2 is not operative and could not be initialized.");
		        }
		        listOfTrafficLightSystems.add(tls2); // Add TLS 2 to the list
		        tls2.initTLSComponents(); // Initialise associated components
		        
		    } catch (Exception e) {
		        System.err.println("Error initializing Traffic Light System 2: " + e.getMessage());
		    }
		}
         
         /**
         * Method Configures the visual recognition parameters for all associated Visual Recognition Systems.
         * 
         * It uses Executor to asynchronously configure of all VRS instances.
         * Using a fixed thread pool to improve performance and reduce configuration time.
         * 
         * - The method logs the successful configuration of each VRS for tracking and verification purposes.
         * - If the thread pool does not terminate within a specified timeout, it attempts to shut down immediately to release resources.
         * 
         * @param scanFrequency The frequency at which each VRS should perform scans.
         * @param scanResolution The resolution or detail level each VRS should use for scans.
         */
         public void configAllVisualRecognitionSystems(int numOfScans, int scanLengthInSeconds) {
        	 
        	     this.numOfVisualRecognitionScans = numOfScans;
        	     this.lengthOfVRScans = scanLengthInSeconds;    // in  seconds
        	 
        	     // Create a thread pool with as many threads as there are VRS instances
             ExecutorService executor = Executors.newFixedThreadPool(listOfTrafficLightSystems.size());
             
             try {
            	     // Iterate over the list of TLSs associated
                 for (TrafficLightSystem tls : listOfTrafficLightSystems) {
                	    // Iterate over the list of VRSs associated to each TLS
                     for (VisualRecognitionSystem vrs : tls.getVisualRecognitionSystems()) {
                         executor.submit(() -> {
                             vrs.setNumOfTrafficScans(numOfScans);
                             vrs.setScanTime(scanLengthInSeconds);
                             System.out.println("Successfully configured VRS " + vrs.getSYSTEMID() + " in TLS " + tls.getSystemId());
                         });
                     }
                 }
             } finally {
                 executor.shutdown();
                 try {
                     if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                         executor.shutdownNow();
                     }
                 } catch (InterruptedException ie) {
                     executor.shutdownNow();
                     Thread.currentThread().interrupt();
                 }
             }
         }
         
         
         /***********************  START OF TRAFFIC CONTROL CYCLE MANAGER ******************/
         
		/***
		 * Method to initialise the whole Traffic Control cycle.
		 * 
		 * - Start the cycle with the initial predefined state
		 * - Initiates Visual Recognition Systems to start data collecting proccess
		 * 
		 * ***/
		public void startTrafficControlCycle(String state) {
			
			// Check if the cycle limit has been reached
	        if (cycleCount >= maxCycles) {
	            System.out.println("Reached the maximum number of cycles.");
	            return;
	        }
	        
			/* time of traffic lights status cycle is the sum of the number of traffic scans 
			 * by the length of each plus 2 seconds 
			 * Those 2 extra seconds a safe time to collect and analize the data from 
			 * the Visual Recognition system and state the set cycle based on that data.
			 * */				
			int cycleTimeInSeconds = (this.lengthOfVRScans * this.numOfVisualRecognitionScans) + 4;
					     
            int greenPhaseLength = cycleTimeInSeconds - 4;  // green state length is equal to the cycle time less 2 seconds
            int yellowPhaseLength = greenPhaseLength + 2 ; // Yellow phase lasts for 2 seconds, and another 2 seconds remains before changin state
            
            System.out.println("\nStart Traffic Controll Cycle " + (cycleCount += 1)  + " with the initial predifined state...");

	          	initGreenPhase(state, greenPhaseLength);     // green phase        	
	            
	          	/** Once the new state is updated, add StateRecord object to list */
	            tlsStateHistory.add(new StateRecord(tls1.getSystemId(), state));  
	            
	          	initYellowPhase( state,  yellowPhaseLength);   // yellow phase  
	          	initTransitToNextCycle(state);                     // transit to netx cycle
  
	      }
	    
		/****
		 * Method manages the green phase
		 * 
		 * @param state
		 * @param greenPhaseLength
		 * **/
		public void initGreenPhase(String state, int greenPhaseLength) {
			this.updateTrafficLightState(state, state.equals("green") ? "red" : "green");
			        
	        System.out.println("\nGREEN PHASE");
            System.out.println("\nTraffic light System 1 state: " + "light 1 " + tls1.getTlA().getState() + "; light 2 " + tls1.getTlB().getState());      
            System.out.println("Traffic light System 2 state:" + "light 1 " + tls2.getTlA().getState() + "; light 2 " + tls2.getTlB().getState());
  	
			try {
				Thread.sleep((greenPhaseLength ) * 1000);
				
			    this.startVRSDataCollection(); // start process of traffic data collection
		        this.analizeTrafficData();     // start data analysing process
		     
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/****
		 * Method manages the yellow phase
		 * 
		 * @param state
		 * @param greenPhaseLength
		 * **/
		public void initYellowPhase(String state, int yellowPhaseLength) {
			
			updateTrafficLightState(state.equals("green") ? "yellow" : "red", state.equals("green") ? "red" : "yellow");
			
			 System.out.println("\nYELLOW PHASE");
             System.out.println("\nTraffic light System 1 state: " + "light 1 " + tls1.getTlA().getState() + "; light 2 " + tls1.getTlB().getState());      
             System.out.println("Traffic light System 2 state:" + "light 1 " + tls2.getTlA().getState() + "; light 2 " + tls2.getTlB().getState());
       
             try {
				Thread.sleep(yellowPhaseLength * 1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		/***
		 * Method starts transition to the next cycle
		 * 
		 * @param the current state
		 * **/
		private void initTransitToNextCycle(String state) {
			
			updateTrafficLightState(state.equals("green") ? "red" : "green", state.equals("green") ? "green" : "red");
			
	        System.out.println("\nTransition to next cycle completed.");
	    }
		
		
		/**
		 * Method responsible for updating the state of Traffic Light Systems for each pahse of the cycle
		 * ***/
		public void updateTrafficLightState(String stateForTls1, String stateForTls2) {
			   tls1.updateLightsState(stateForTls1);
		       tls2.updateLightsState(stateForTls2);
		       System.out.println("\nUpdated states - TLS1: " + stateForTls1 + ", TLS2: " + stateForTls2);    
		}
		
		
		/***********************  END TRAFFIC CONTROL CYCLE MANAGER ******************/
		
		
		/**
		 * Method start procces of traffic data collection of All Visual Recognition Systems
		 * **/
		public void startVRSDataCollection() {
			
			// Create a thread pool with as many threads as there are TLS instances
	        ExecutorService executor = Executors.newFixedThreadPool(listOfTrafficLightSystems.size());
			
	         // Iterates through the list of TLSs associated to this TCS
			 for ( TrafficLightSystem tls :  listOfTrafficLightSystems) {
				 executor.submit(() -> {
	               tls.startVRDataCollection(); //VRS are initialized and begin data collection.
				 });
	        }
			// Shutdown the executor after starting all tasks
		        executor.shutdown();
		}
		
		/**
		 * Method starts the process of traffic data analysing, and reports detailing the traffic data collected 
		 * by all Visual Recognition Systems (VRS) within each Traffic Light System (TLS).
		 * 
		 * It retrieves the data collected from each VRS associated to each of the TLS,
		 * and stores data in a key-value structure Map<TLS_id, totalOfVehicles> to then pass it into compareTLSTrafficData()
		 * 
		 * **/
		public void analizeTrafficData() {
			
		    Map<Integer, Integer> tlsVehicleCounts = new HashMap<>(); // map to store the total vehicles collected for each TLS id
                   
			int totalVehicles = 0;
			
			    // loop through the list of Traffic Light Systems
				for(TrafficLightSystem tls : listOfTrafficLightSystems) {
					System.out.println("Traffic Light System " + tls.getSystemId() + "**REPORT**");
					
					// loop through the list of Visual Recognition Ssystems associated to each TLS and retrieve total vehicles count
					for(VisualRecognitionSystem vrs : tls.getVisualRecognitionSystems()) {

			            totalVehicles += vrs.getTotalVehicles();   // grab total vehicle count in current VRS
			             
						System.out.println("VRS " + vrs.getSYSTEMID() + "**Total vehicles last scan: **");
						System.out.println("Total vehicles " + vrs.getTotalVehicles());
					}
					
					
			       tlsVehicleCounts.put(tls.getSystemId(), totalVehicles);  // Store total vehicles count in map
				}
				 
				compareTLSTrafficData(tlsVehicleCounts);  // compare data retrieved
		}
		
		
		/**
		 * Method compares the traffic data of each of the Traffic Ligth Systems, 
		 * and sets the next state of the Traffic Light Systems for the next traffic control cycle.
		 * 
		 * The method identifies which TLS has higher or lower vehicle traffic and performs actions based on these comparisons.
		 * 
		 * 
		 * */
		public void compareTLSTrafficData(Map<Integer, Integer> tlsVehicleCounts) {
			
			// check if  hashmap is empty or null
		    if (tlsVehicleCounts == null || tlsVehicleCounts.isEmpty()) {
		        System.out.println("No traffic data available to compare.");
		        return;
		    }
		    
		    // store the Traffic Systems id form the map into an array
		    Integer[] tlsIds = tlsVehicleCounts.keySet().toArray(new Integer[0]);
		    
		    int tls1id = tlsIds[0];
		    int tls2id = tlsIds[1];
		    
		    // use the Traffic Ssystems IDs to grab and store their value pair from the map
		    Integer vehiclesCountTLS1 = tlsVehicleCounts.get(tlsIds[0]);
	        Integer vehiclesCountTLS2 = tlsVehicleCounts.get(tlsIds[1]);
	        
	        if(vehiclesCountTLS1 >= vehiclesCountTLS2) {
	        	    setNextCycle(tls1id, "green");
	        }else {
	            setNextCycle(tls2id, "green");
	        } 
	        
		}
		
		/***
		* Method takes 2 parameters
		* - tlsId is the id of the TLS that should be green on next cycle
		* - state is always green
		* **/
		public void setNextCycle(int nextTLSId, String nextState) {
		
			     int tlsStateHistorySize = tlsStateHistory.size();
			     
				// check if states history has more than 1 records stored
				if(tlsStateHistory.size() >= 2) {
					
					// grab values of the last Traffic Light System with a green state 
			        StateRecord currentRecord = tlsStateHistory.get(tlsStateHistorySize - 1);
					
					// grab values of the last Traffic Light System with a green state 
			        StateRecord lastRecord = tlsStateHistory.get(tlsStateHistorySize - 2);
					
			        // Preventing three consecutive green states for the same TLS
					if(currentRecord.getState().equals("green") && currentRecord.getTLSID() == lastRecord.getTLSID() &&
						lastRecord.getState().equals("green")) {
		        	    System.out.println("Traffic Light System 1 reports higher traffic density but can't run for 3 consecutive time with same state"
		        	    		+ " so nexts cycle will change. Total vehicles reported ");

						startTrafficControlCycle("red");
					}else {
					    // Proceed with the proposed green state if no conflict
					    startTrafficControlCycle(nextState);
					}
			    } else {
				    // continue to next cycle with the previously defined state if not enough history records
					startTrafficControlCycle(nextState);
					System.out.println("Traffic Light System 2 reports higher traffic density. " 
					+ "Therefore nexts cycle will run with green state");
			}
	}
				
				
		

		// setters
		
		/**
		 * Set trafficLightSystem 1
		 * */
		public void setTls1(TrafficLightSystem tls1) {
			this.tls1 = tls1;
		}
		
		/**
		 * Set trafficLightSystem 1
		 * */
		public void setTls2(TrafficLightSystem tls2) {
			this.tls2 = tls2;
		}
		
		/**
		 * Set Traffic Control System status
		 * */
		public void setIsOperative(boolean status) {
			this.isOperative = status;
		}
		
		
		
		// getters
		
		/**
		 * Get the systemID
		 */
		public int getSystemID() {
			return systemID;
		}

		/**
		 * Get trafficLightSystem 1
		 * */
	    public TrafficLightSystem getTls1() {
			return tls1;
		}

	    /**
		 * Get trafficLightSystem 1
		 * */
		public TrafficLightSystem getTls2() {
			return tls2;
		}

		/**
		 * Get Traffic Control System status
		 * */
		public boolean isOperative() {
			return isOperative;
		}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
	}

}
