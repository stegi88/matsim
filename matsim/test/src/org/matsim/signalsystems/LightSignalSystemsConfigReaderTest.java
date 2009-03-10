/* *********************************************************************** *
 * project: org.matsim.*
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

import org.matsim.basic.signalsystemsconfig.BasicPlanBasedSignalSystemControlInfo;
import org.matsim.basic.signalsystemsconfig.BasicSignalGroupSettings;
import org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfiguration;
import org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfigurations;
import org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfigurationsImpl;
import org.matsim.basic.signalsystemsconfig.BasicSignalSystemPlan;
import org.matsim.basic.v01.IdImpl;
import org.matsim.interfaces.basic.v01.Id;
import org.matsim.testcases.MatsimTestCase;

/**
 * Test for the readers and writers of the (light)signalSystemConfigurations_v1.*.xsd
 * file formats.
 * @author dgrether
 */
public class LightSignalSystemsConfigReaderTest extends MatsimTestCase {

  private static final String TESTXML  = "testSignalSystemConfigurations_v1.1.xml";

  private Id id8 = new IdImpl("8");

  private Id id5 = new IdImpl("5");
  
  private Id id23 = new IdImpl("23");
  
  private Id id42 = new IdImpl("42");

  
  
  public void testParser() {
  	BasicSignalSystemConfigurations lssConfigs = new BasicSignalSystemConfigurationsImpl();
  	MatsimSignalSystemConfigurationsReader reader = new MatsimSignalSystemConfigurationsReader(lssConfigs);
  	reader.readFile(this.getPackageInputDirectory() + TESTXML);
  }
  
  public void testWriter() {
  	String testoutput = this.getOutputDirectory()  + "testLssConfigOutput.xml";
  	//read the test file
  	BasicSignalSystemConfigurations lssConfigs = new BasicSignalSystemConfigurationsImpl();
  	MatsimSignalSystemConfigurationsReader reader = new MatsimSignalSystemConfigurationsReader(lssConfigs);
  	reader.readFile(this.getPackageInputDirectory() + TESTXML);

  	//write the test file
  	MatsimSignalSystemConfigurationsWriter writer = new MatsimSignalSystemConfigurationsWriter(lssConfigs);
  	writer.writeFile(testoutput);
  	
  	lssConfigs = new BasicSignalSystemConfigurationsImpl();
  	reader = new MatsimSignalSystemConfigurationsReader(lssConfigs);
  	reader.readFile(testoutput);
  	checkContent(lssConfigs);
  }

	private void checkContent(BasicSignalSystemConfigurations lssConfigs) {
		assertEquals(2, lssConfigs.getSignalSystemConfigurations().size());
		//test first
		BasicSignalSystemConfiguration lssConfiguration = lssConfigs.getSignalSystemConfigurations().get(id23);
		assertNotNull(lssConfiguration);
		assertEquals(id23, lssConfiguration.getSignalSystemId());
		BasicPlanBasedSignalSystemControlInfo controlInfo = (BasicPlanBasedSignalSystemControlInfo) lssConfiguration.getControlInfo();
		assertNotNull(controlInfo);
		BasicSignalSystemPlan plan =   controlInfo.getPlans().get(id5);
		assertNotNull(plan);
		assertEquals(id5, plan.getId());
		assertEquals(0.0, plan.getStartTime(), EPSILON);
		assertEquals(0.0, plan.getEndTime(), EPSILON);
		assertEquals(Integer.valueOf(40), plan.getCirculationTime());
		assertEquals(Integer.valueOf(3), plan.getSyncronizationOffset());
		
		assertEquals(1, plan.getGroupConfigs().size());
		BasicSignalGroupSettings groupConfig = plan.getGroupConfigs().get(id23);
		assertNotNull(groupConfig);
		assertEquals(0.0, groupConfig.getRoughCast(), EPSILON);
		assertEquals(45.0, groupConfig.getDropping(), EPSILON);
		assertEquals(Integer.valueOf(2), groupConfig.getInterimTimeRoughcast());
		assertEquals(Integer.valueOf(3), groupConfig.getInterimTimeDropping());
		
		//test second
		lssConfiguration = lssConfigs.getSignalSystemConfigurations().get(id42);
		assertNotNull(lssConfiguration);
		assertEquals(id42, lssConfiguration.getSignalSystemId());
		controlInfo = (BasicPlanBasedSignalSystemControlInfo) lssConfiguration.getControlInfo();
		assertNotNull(controlInfo);
		plan =   controlInfo.getPlans().get(id8);
		assertNotNull(plan);
		assertEquals(id8, plan.getId());
		assertEquals(0.0, plan.getStartTime(), EPSILON);
		assertEquals(0.0, plan.getEndTime(), EPSILON);
		assertEquals(Integer.valueOf(60), plan.getCirculationTime());
		assertNull(plan.getSyncronizationOffset());
		assertEquals(1, plan.getGroupConfigs().size());
		groupConfig = plan.getGroupConfigs().get(id23);
		assertNotNull(groupConfig);
		assertEquals(0.0, groupConfig.getRoughCast(), EPSILON);
		assertEquals(45.0, groupConfig.getDropping(), EPSILON);
		assertNull(groupConfig.getInterimTimeRoughcast());
		assertNull(groupConfig.getInterimTimeDropping());
	}  
	
}
