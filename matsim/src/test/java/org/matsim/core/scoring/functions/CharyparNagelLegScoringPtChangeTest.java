/* *********************************************************************** *
 * project: org.matsim.*
 * CharyparNagelLegScoringPtChangeTest.java
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
package org.matsim.core.scoring.functions;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.api.experimental.events.Event;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.population.LegImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.MatsimTestUtils;

/**
 * @author thibautd
 */
public class CharyparNagelLegScoringPtChangeTest {
	/**
	 * Tests whether changing the pt params changes the score for a minimal plan consisting
	 * of a single car leg.
	 * This is based on the (bugged) code for line change as of 2013.05.10.
	 * It is quite hard to guarantee that the test is thourough,
	 * as the scoring function is allowed to get arbitrary events,
	 * and may do funny things (such as using end of activities as
	 * the start of the waiting).
	 */
	@Test
	public void testPtParamsDoNotInfluenceCarScore() throws Exception {
		final Network network = createNetwork();
		final CharyparNagelLegScoring scoring1 = createScoring( 1 , network );
		final CharyparNagelLegScoring scoring2 = createScoring( 2 , network );

		final Leg leg = new LegImpl( TransportMode.car );
		leg.setDepartureTime( 0 );
		leg.setTravelTime( 120 );

		// "simulate"
		final EventsManager events = EventsUtils.createEventsManager();
		final Event endFirstAct =  events.getFactory().createActivityEndEvent(
				leg.getDepartureTime(),
				new IdImpl( 1 ),
				new IdImpl( 1 ),
				new IdImpl( 1 ),
				"start");
		scoring1.handleEvent( endFirstAct );
		scoring2.handleEvent( endFirstAct );

		final Event departure = events.getFactory().createAgentDepartureEvent(
				leg.getDepartureTime(),
				new IdImpl( 1 ),
				new IdImpl( 1 ),
				leg.getMode() );
		scoring1.handleEvent( departure );
		scoring2.handleEvent( departure );

		final Event enterVehicle = events.getFactory().createPersonEntersVehicleEvent(
				leg.getDepartureTime() + 100,
				new IdImpl( 1 ),
				new IdImpl( 1 ));
		scoring1.handleEvent( enterVehicle );
		scoring2.handleEvent( enterVehicle );

		final Event leaveVehicle = events.getFactory().createPersonLeavesVehicleEvent(
				leg.getDepartureTime() + leg.getTravelTime(),
				new IdImpl( 1 ),
				new IdImpl( 1 ));
		scoring1.handleEvent( leaveVehicle );
		scoring2.handleEvent( leaveVehicle );

		final Event arrival = events.getFactory().createAgentArrivalEvent(
				leg.getDepartureTime() + leg.getTravelTime(),
				new IdImpl( 1 ),
				new IdImpl( 1 ),
				leg.getMode() );
		scoring1.handleEvent( arrival );
		scoring2.handleEvent( arrival );

		// "scoring"
		scoring1.startLeg( leg.getDepartureTime() , leg );
		scoring1.endLeg( leg.getDepartureTime() + leg.getTravelTime() );
		scoring1.finish();

		scoring2.startLeg( leg.getDepartureTime() , leg );
		scoring2.endLeg( leg.getDepartureTime() + leg.getTravelTime() );
		scoring2.finish();

		// here, we should get the same score.
		Assert.assertEquals(
				"score for car leg differs when changing pt parameters! Probably a problem in line change handling.",
				scoring1.getScore(),
				scoring2.getScore(),
				MatsimTestUtils.EPSILON );
	}

	private static CharyparNagelLegScoring createScoring(
			final int seed,
			final Network network) {
		final Random random = new Random( seed );

		final PlanCalcScoreConfigGroup conf = new PlanCalcScoreConfigGroup();
		conf.setMarginalUtlOfWaitingPt_utils_hr( random.nextDouble() * 1000 );
		conf.setMonetaryDistanceCostRatePt( random.nextDouble() * 1000 );
		conf.setTravelingPt_utils_hr( random.nextDouble() * 1000 );
		conf.setUtilityOfLineSwitch( random.nextDouble() * 1000 );

		return new CharyparNagelLegScoring(
				new CharyparNagelScoringParameters( conf ),
				network);
	}

	private static Network createNetwork() {
		final Network network = ScenarioUtils.createScenario( ConfigUtils.createConfig() ).getNetwork();

		final Node node1 = network.getFactory().createNode( new IdImpl( 1 ) , new CoordImpl( 0 , 0 ) );
		network.addNode( node1 );
		final Node node2 = network.getFactory().createNode( new IdImpl( 2 ) , new CoordImpl( 1 , 1 ) );
		network.addNode( node2 );
		network.addLink( network.getFactory().createLink( new IdImpl( 1 ) , node1 , node2 ) );

		return network;
	}
}

