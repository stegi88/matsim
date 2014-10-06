/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package playground.agarwalamit.analysis.legModeHandler;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.handler.EventHandler;
import org.matsim.core.utils.io.IOUtils;

import playground.vsp.analysis.modules.AbstractAnalyisModule;

/**
 * @author amit
 */
public class LegModeRouteDistanceDistributionAnalyzer extends AbstractAnalyisModule {

	private Scenario scenario;
	private final List<Integer> distanceClasses;
	private SortedSet<String> usedModes;
	private final Logger log = Logger.getLogger(LegModeRouteDistanceDistributionAnalyzer.class);

	private SortedMap<String, SortedMap<Integer, Integer>> mode2DistanceClass2LegCount ;
	private SortedMap<String, Map<Id<Person>, List<Double>>> mode2PersonId2dist;
	private LegModeRouteDistanceDistributionHandler lmrdh;
	private String eventsFile;

	public LegModeRouteDistanceDistributionAnalyzer() {
		super(LegModeRouteDistanceDistributionAnalyzer.class.getSimpleName());
		this.log.info("enabled");

		this.distanceClasses = new ArrayList<Integer>();
		this.usedModes = new TreeSet<String>();
		this.mode2PersonId2dist = new TreeMap<String, Map<Id<Person>,List<Double>>>();
		this.mode2DistanceClass2LegCount = new TreeMap<String, SortedMap<Integer,Integer>>();
	}



	public void init(Scenario sc, String eventsFile){
		this.scenario = sc;
		this.lmrdh = new LegModeRouteDistanceDistributionHandler(scenario);
		this.eventsFile = eventsFile;
	}

	@Override
	public List<EventHandler> getEventHandler() {
		return null;
	}

	@Override
	public void preProcessData() {
		EventsManager manager = EventsUtils.createEventsManager();
		MatsimEventsReader reader = new MatsimEventsReader(manager);
		manager.addHandler(lmrdh);
		reader.readFile(eventsFile);

		this.usedModes = lmrdh.getUsedModes();
		this.log.info("The following transport modes are considered: " + this.usedModes);
		initializeDistanceClasses();
		this.mode2PersonId2dist = lmrdh.getMode2PersonId2TravelDistances();

		for(String mode:this.usedModes){
			SortedMap<Integer, Integer> distClass2Legs = new TreeMap<Integer, Integer>();
			for(int i: this.distanceClasses){
				distClass2Legs.put(i, 0);
			}
			this.mode2DistanceClass2LegCount.put(mode, distClass2Legs);
		}
	}

	@Override
	public void postProcessData() {
		calculateMode2DistanceClass2LegCount();
	}

	private void calculateMode2DistanceClass2LegCount() {
		for(String mode : mode2PersonId2dist.keySet()){
			for(Id<Person> personId :mode2PersonId2dist.get(mode).keySet()){
				for(int index=0;index<mode2PersonId2dist.get(mode).get(personId).size();index++){
					double distance =mode2PersonId2dist.get(mode).get(personId).get(index) ;
					for(int i=0;i<this.distanceClasses.size()-1;i++){
						if(distance > this.distanceClasses.get(i) && distance <= this.distanceClasses.get(i + 1)){
							SortedMap<Integer, Integer> distanceClass2NoOfLegs = this.mode2DistanceClass2LegCount.get(mode);	
							int oldLeg = distanceClass2NoOfLegs.get(this.distanceClasses.get(i+1));
							int newLeg = oldLeg+1;
							distanceClass2NoOfLegs.put(this.distanceClasses.get(i+1), newLeg);
						} 
					}
				}
			}
		}
	}
	@Override
	public void writeResults(String outputFolder) {
		String outFile = outputFolder + "legModeRouteDistanceDistribution2.txt";
		try{
			BufferedWriter writer1 = IOUtils.getBufferedWriter(outFile);
			writer1.write("class");
			for(String mode : this.usedModes){
				writer1.write("\t" + mode);
			}
			writer1.write("\t" + "sum");
			writer1.write("\n");
			for(int i = 0; i < this.distanceClasses.size() - 1 ; i++){
				writer1.write(this.distanceClasses.get(i+1) + "\t");
				Integer totalLegsInDistanceClass = 0;
				for(String mode : this.usedModes){
					Integer modeLegs = null;
					modeLegs = this.mode2DistanceClass2LegCount.get(mode).get(this.distanceClasses.get(i + 1));
					totalLegsInDistanceClass = totalLegsInDistanceClass + modeLegs;
					writer1.write(modeLegs.toString() + "\t");
				}
				writer1.write(totalLegsInDistanceClass.toString());
				writer1.write("\n");
			}
			writer1.close();
			this.log.info("Finished writing output to " + outFile);
		}catch (Exception e){
			this.log.error("Data is not written. Reason " + e.getMessage());
		}
	}

	private void initializeDistanceClasses() {
		double longestDistance = lmrdh.getLongestDistance();
		this.log.info("The longest distance is found to be: " + longestDistance);
		int endOfDistanceClass = 0;
		int classCounter = 0;
		this.distanceClasses.add(endOfDistanceClass);

		while(endOfDistanceClass <= longestDistance){
			endOfDistanceClass = 100 * (int) Math.pow(2, classCounter);
			classCounter++;
			this.distanceClasses.add(endOfDistanceClass);
		}
		this.log.info("The following distance classes were defined: " + this.distanceClasses);
	}

	public SortedMap<String, SortedMap<Integer, Integer>> getMode2DistanceClass2LegCount() {
		return this.mode2DistanceClass2LegCount;
	}

	public SortedMap<String, Map<Id<Person>, List<Double>>> getMode2PersonId2RouteDistances(){
		return this.mode2PersonId2dist;
	}
}
