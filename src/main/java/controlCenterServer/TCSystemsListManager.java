/**
 * Javier Bastande
 */
package controlCenterServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import trafficControlSystem.TrafficControlSystem;

/**
 * Singleton class represents a list with all the Traffic Control Systems associated with each Traffic Control System.
 * 
 * Class acts as a manager of this list to manage functionalities such as 
 * add, or remove Traffic Control Systems of the Control Centre network
 * 
 */
public class TCSystemsListManager implements Iterable<TrafficControlSystem>{

	// vars
	private static TCSystemsListManager instance;
    private List<TrafficControlSystem> trafficControlSystems;

    // Pivate constructor
    private TCSystemsListManager() {
        trafficControlSystems = new ArrayList<>();   // initialize arrayList
    }
    
    
    /***
     * Static method initialise an intance of TCSystemsListManger
     * **/
    public static synchronized TCSystemsListManager getInstance() {
        if (instance == null) {
            instance = new TCSystemsListManager();
        }
        return instance;
    }

    public List<TrafficControlSystem> getTrafficControlSystems() {
        return trafficControlSystems;
    }
    
    
    // helper methods
    
    /***
	 * Method adds a new Traffic Control System to the list 
	 * of Traffic Control Systems that Control Centre System manages
	 * 
	 */
	public void addTrafficContolSystem(TrafficControlSystem newTCS) {
		trafficControlSystems.add(newTCS);   
	}


	@Override
	public Iterator<TrafficControlSystem> iterator() {
		return trafficControlSystems.iterator();
	}
}
