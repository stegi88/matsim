/* *********************************************************************** *
 * project: org.matsim.*
 * TravelTimeTestFourWay
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.signalsystems;

import java.io.BufferedWriter;
import java.io.IOException;

import org.matsim.config.Config;
import org.matsim.controler.ScenarioData;
import org.matsim.events.ActEndEvent;
import org.matsim.events.ActStartEvent;
import org.matsim.events.AgentArrivalEvent;
import org.matsim.events.AgentDepartureEvent;
import org.matsim.events.AgentWait2LinkEvent;
import org.matsim.events.Events;
import org.matsim.events.LinkEnterEvent;
import org.matsim.events.LinkLeaveEvent;
import org.matsim.events.handler.ActEndEventHandler;
import org.matsim.events.handler.ActStartEventHandler;
import org.matsim.events.handler.AgentArrivalEventHandler;
import org.matsim.events.handler.AgentDepartureEventHandler;
import org.matsim.events.handler.AgentWait2LinkEventHandler;
import org.matsim.events.handler.LinkEnterEventHandler;
import org.matsim.events.handler.LinkLeaveEventHandler;
import org.matsim.mobsim.queuesim.QueueSimulation;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.utils.CRCChecksum;
import org.matsim.utils.io.IOUtils;

/**
 * @author aneumann
 * @author dgrether
 */
public class TravelTimeTestFourWays extends MatsimTestCase implements	LinkLeaveEventHandler, LinkEnterEventHandler, ActEndEventHandler, ActStartEventHandler, AgentArrivalEventHandler, AgentDepartureEventHandler, AgentWait2LinkEventHandler {

	BufferedWriter writer = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTrafficLightIntersection4arms() {
		Config conf = loadConfig(this.getClassInputDirectory() + "config.xml");
		String laneDefinitions = this.getClassInputDirectory()
				+ "testLaneDefinitions_v1.1.xml";
		String lsaDefinition = this.getClassInputDirectory()
				+ "testSignalSystems_v1.1.xml";
		String lsaConfig = this.getClassInputDirectory()
				+ "testSignalSystemConfigurations_v1.1.xml";
		conf.network().setLaneDefinitionsFile(laneDefinitions);
		conf.signalSystems().setSignalSystemFile(lsaDefinition);
		conf.signalSystems().setSignalSystemConfigFile(lsaConfig);
		ScenarioData data = new ScenarioData(conf);

		Events events = new Events();
		events.addHandler(this);
		String tempout = this.getOutputDirectory() + "temp.txt.gz";
		try {
			this.writer = IOUtils.getBufferedWriter(tempout, true);
			QueueSimulation sim = new QueueSimulation(data.getNetwork(), data.getPopulation(), events);
			sim.setLaneDefinitions(data.getLaneDefinitions());
			sim.setSignalSystems(data.getSignalSystems(), data.getSignalSystemsConfiguration());
			sim.run();
			this.writer.flush();
			this.writer.close();
			assertEquals(CRCChecksum.getCRCFromFile(this.getClassInputDirectory() + "reference4armsWoUTurn.txt.gz"), CRCChecksum.getCRCFromFile(tempout));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testTrafficLightIntersection4armsWithUTurn() {
		Config conf = loadConfig(this.getClassInputDirectory() + "config.xml");
		String laneDefinitions = this.getClassInputDirectory()
				+ "testLaneDefinitions_v1.1.xml";
		String lsaDefinition = this.getClassInputDirectory()
				+ "testSignalSystems_v1.1.xml";
		String lsaConfig = this.getClassInputDirectory()
				+ "testSignalSystemConfigurations_v1.1.xml";
		conf.network().setLaneDefinitionsFile(laneDefinitions);
		conf.signalSystems().setSignalSystemFile(lsaDefinition);
		conf.signalSystems().setSignalSystemConfigFile(lsaConfig);
		conf.plans().setInputFile(this.getClassInputDirectory() + "plans_uturn.xml.gz");
		ScenarioData data = new ScenarioData(conf);
		Events events = new Events();
		events.addHandler(this);
		String tempout = this.getOutputDirectory() + "temp.txt.gz";
		try {
			this.writer = IOUtils.getBufferedWriter(tempout, true);
//			new QSim(events, data.getPopulation(), data.getNetwork(), false, newLSADef, newLSADefCfg).run();
			QueueSimulation sim = new QueueSimulation(data.getNetwork(), data.getPopulation(), events);
			sim.setLaneDefinitions(data.getLaneDefinitions());
			sim.setSignalSystems(data.getSignalSystems(), data.getSignalSystemsConfiguration());
			sim.run();
			this.writer.flush();
			this.writer.close();
			assertEquals(CRCChecksum.getCRCFromFile(tempout),	CRCChecksum.getCRCFromFile(this.getClassInputDirectory() + "reference_uturn.txt.gz"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleEvent(final LinkEnterEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleEvent(final LinkLeaveEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reset(final int iteration) {
		// Not used in that TestCase
	}

	public void handleEvent(final ActEndEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleEvent(final ActStartEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleEvent(final AgentArrivalEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleEvent(final AgentDepartureEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handleEvent(final AgentWait2LinkEvent event) {
		try {
			this.writer.write(event.toString());
			this.writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
