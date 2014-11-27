/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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

/**
 * 
 */
package playground.ikaddoura.noise2;

import org.apache.log4j.Logger;
import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.controler.listener.StartupListener;


/**
 * @author ikaddoura
 *
 */

public class NoiseCalculationOnline2 implements BeforeMobsimListener, AfterMobsimListener , StartupListener {
	private static final Logger log = Logger.getLogger(NoiseCalculationOnline2.class);
	
	private NoiseParameters noiseParameters;
	private NoiseContext noiseContext;
	private NoiseTimeTracker timeTracker;
			
	public NoiseCalculationOnline2(NoiseParameters noiseParameters) {
		this.noiseParameters = noiseParameters;
	}

	@Override
	public void notifyStartup(StartupEvent event) {
		
		log.info("Initialization...");
		
		this.noiseContext = new NoiseContext(event.getControler().getScenario(), noiseParameters);
		this.noiseContext.initialize();
		NoiseWriter2.writeReceiverPoints(noiseContext, event.getControler().getConfig().controler().getOutputDirectory() + "/receiverPoints/");
		
		log.info("Initialization... Done.");

		timeTracker = new NoiseTimeTracker(noiseContext, event.getControler().getEvents(), event.getControler().getConfig().controler().getOutputDirectory() + "/ITERS/");			
		event.getControler().getEvents().addHandler(timeTracker);
	}
	
	@Override
	public void notifyBeforeMobsim(BeforeMobsimEvent event) {
		log.info("Resetting noise immissions, activity information and damages...");
		for (ReceiverPoint rp : this.noiseContext.getReceiverPoints().values()) {
			// TODO
		}
		log.info("Resetting noise immissions, activity information and damages... Done.");
	}
	
	@Override
	public void notifyAfterMobsim(AfterMobsimEvent event) {
				
		timeTracker.computeFinalTimeInterval();
	}

	NoiseContext getNoiseContext() {
		return noiseContext;
	}
	
}