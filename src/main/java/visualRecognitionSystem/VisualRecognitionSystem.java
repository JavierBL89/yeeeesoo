

	/**
	 * 
	 */
	package visualRecognitionSystem;

	import java.sql.Time;
	import java.time.LocalTime;
	import java.util.Random;

	
	 /*
	 * Class represents the hardware and software of a Visual Recognition System.
	 * It encapsulates the configuration of visual recognition processes and interacts with the TrafficDataCollector 
	 * to initiate data collection cycles.
	 * This system is associated with a specific traffic control point(traffic light) by traffic Light ID.
	 * 
	 * Responsibilities:
	 * - Configuring visual recognition parameters such as the number of traffic scans and the duration of each scan.
	 * - Initiating and managing the data collection cycle through the integrated TrafficDataCollector.
	 * - Handles anomaly detection .
	 * 
	 * Usage:
	 * An instance of the VisualRecognitionSystem should be created for each traffic control point where visual recognition is employed. 
	 * The system allows for dynamic configuration of recognition parameters, enabling the traffic management system to adjust to real-time 
	 * conditions effectively. 
	 */
	public class VisualRecognitionSystem {
		
		// vars
		private static int nextSystemId = 9022;
		
		private int systemID;
		private int trafficLightID;
		private int trafficLightSystemID;
		private int totalVehicles;
		private int numOfTrafficScans;
		private int scanLengthInSeconds;
		private int anomalies;

		// objects
		private TrafficDataCollector tdc;            // data collector
		
		// constructors
		
		public VisualRecognitionSystem() {
			this.systemID = ++nextSystemId;  // auto increment id
			this.trafficLightSystemID = 0;   // Traffic Light System id the VRS is associated to
			this.numOfTrafficScans = 0;
			this.totalVehicles = 0;
			this.scanLengthInSeconds = 0;
			this.anomalies = 0;
			this.tdc = new TrafficDataCollector();   // instantiate a Traffic Data Collector object
		};
		
		
		/**
		 * Initialises a new VisualRecognitionSystem object with custom parameters
		 * **/
		public VisualRecognitionSystem(int trafficLightId, int trafficLightSystemID) {
			this.systemID = ++nextSystemId;  // auto increment id
			this.trafficLightSystemID = trafficLightSystemID;   // Traffic Light System id the VRS is associated to
			this.trafficLightID = trafficLightId;   // Traffic Light System id the VRS is associated to
			this.numOfTrafficScans = 0;
			this.totalVehicles = 0;
			this.scanLengthInSeconds = 0;
			this.anomalies = 0;
			this.tdc = new TrafficDataCollector();   // instantiate a Traffic Data Collector object
		}
		

		//setters 

		/**
		 * Set trafficLightID 
		 */
		public void setTrafficLightID(int trafficLightID) {
			this.trafficLightID = trafficLightID;
		}

		/**
		 * Set trafficLightSsytemID the VRS is associated to 
		 */
		public void setTrafficLightSystemID(int TrafficLightSystemID) {
			this.trafficLightID = TrafficLightSystemID;
		} 
		
		/**
		* Set number of micro traffic scans
		*/
	    public void setNumOfTrafficScans(int numOfTrafficScans) {
		   this.numOfTrafficScans = numOfTrafficScans;
	    }

		/**
		* Set length of each micro traffic scan by seconds
	    */
	    public void setScanTime(int scanLengthInSeconds) {
			this.scanLengthInSeconds = scanLengthInSeconds;
	    }
		
	    
		// getters
		
		/**
		 * Get Traffic Light id the VRS is associated to
		 */
		public int getTrafficLightID() {
			return trafficLightID;
		}
		
		/**
		 * Get getNumOfTrafficScans
		 */
		public int getNumOfTrafficScans() {
			return numOfTrafficScans;
		}

		
		/**
		 * Get total of Vehicles per scan
		 */

		public int getTotalVehicles() {
			
			this.totalVehicles = (tdc.getCarCounter() + tdc.getBikeCounter() + tdc.getBusCounter() + tdc.getTruckCounter());
			return this.totalVehicles;
		}
		
		/**
		 * Get the number of anomalies found
		 */
		public int getAnomalies() {
			return tdc.getAnomalies();
		}
		
		/**
		 * Get trafficLightSsytemID the VRS is associated to 
		 */
		public int getTrafficLightSystemID(int TrafficLightSystemID) {
			return TrafficLightSystemID;
		} 
		
		/**
		 * Get getSYSTEMID
		 */
		public int getSYSTEMID() {
			return systemID;
		}

		/**
		* Get length(on seconds) of each micro Traffic Scan
		* @return the scanTime
		*/
		public int getScanTimeInNanoSeconds() {
			return scanLengthInSeconds;
		}
		
		
		// helper methods
		
	    /** 
	    * Method to set visual recognition configurations.
	    * - numOfTrafficScans is the number of micro scans per full scan cycle
		* - scanTime is the length in seconds of each micro scan 
	    */
		public void configVisualRecognition(int numOfTrafficScans, int scanLengthInSeconds) {
			 this.numOfTrafficScans = numOfTrafficScans;
		     this.scanLengthInSeconds = scanLengthInSeconds;
		}
		
		/**
		 * Methos responsible for startting visual recognition proccess
		 * **/
		public void startDataCollectorCycle() {
		
	            tdc.startDataCollector(this.numOfTrafficScans, this.scanLengthInSeconds);
		}
		
		/**
		 * Methos responsible for startting visual recognition proccess
		 * **/
		public void printDetailedScanReport() {
			  tdc.printVehiclesCount();
		}
		
		@Override 
		public String toString() {
			String str = "";
			str += "Current Traffic Control Configuration:";
			str += "A traffic Scan cycle consists of " + this.getNumOfTrafficScans() 
					   + " micro scans of " + this.getScanTimeInNanoSeconds() + " seconds length each.";
					
		     return str;
		}	

		/**
		 * @param args
		 */
		public static void main(String[] args) {
		/*	VisualRecognitionSystem vrs = new VisualRecognitionSystem();
			vrs.configVisualRecognition(3, 2);
			vrs.startDataCollectorCycle();
			vrs.getAnomalies();
			System.out.println(vrs.getTotalVehicles());
			System.out.print("-----------------");
			
			VisualRecognitionSystem vrs2 = new VisualRecognitionSystem();
			vrs2.configVisualRecognition(3, 2);
			vrs2.startDataCollectorCycle();
			vrs2.getAnomalies();
			System.out.println(vrs2.getTotalVehicles());  */
		}
	

	}
