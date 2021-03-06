package playground.balac.freefloating.qsim;

import java.util.ArrayList;

import org.matsim.api.core.v01.network.Link;

import playground.balac.allcsmodestest.facilities.CarSharingStation;

public class FreeFloatingStation implements CarSharingStation{

	
	private Link link;
	private int numberOfVehicles;
	private ArrayList<String> vehicleIDs = new ArrayList<String>();
	
	public FreeFloatingStation(Link link, int numberOfVehicles, ArrayList<String> vehicleIDs) {
		
		this.link = link;
		this.numberOfVehicles = numberOfVehicles;
		this.vehicleIDs = vehicleIDs;
	}
	
	public int getNumberOfVehicles() {
		
		return numberOfVehicles;
	}
	
	public Link getLink() {
		
		return link;
	}
	
	public ArrayList<String> getIDs() {
		
		return vehicleIDs;
	}
	
}
