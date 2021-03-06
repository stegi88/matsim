/* *********************************************************************** *
 * project: org.matsim.*
 * FacilitiesSummary.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package playground.balmermi.toggenburg.modules;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.facilities.ActivityFacilitiesImpl;
import org.matsim.facilities.ActivityFacility;
import org.matsim.facilities.ActivityFacilityImpl;
import org.matsim.facilities.ActivityOption;
import org.matsim.facilities.ActivityOptionImpl;

public class FacilitiesAnalysis {

	//////////////////////////////////////////////////////////////////////
	// member variables
	//////////////////////////////////////////////////////////////////////

	private final static Logger log = Logger.getLogger(FacilitiesAnalysis.class);

	//////////////////////////////////////////////////////////////////////
	// constructors
	//////////////////////////////////////////////////////////////////////

	public FacilitiesAnalysis() {
		log.info("init " + this.getClass().getName() + " module...");
		log.info("done.");
	}

	//////////////////////////////////////////////////////////////////////
	// private methods methods
	//////////////////////////////////////////////////////////////////////

	private final Set<String> getActOptTypes(ActivityFacilitiesImpl facilities) {
		log.info("  extract actOptTypes...");
		Set<String> actOptTypes = new TreeSet<String>();
		for (ActivityFacility facility : facilities.getFacilities().values()) {
			for (ActivityOption actOpt : facility.getActivityOptions().values()) {
				actOptTypes.add(actOpt.getType());
			}
		}
		log.info("  => "+actOptTypes.size()+" actOpts found.");
		log.info("  done.");
		return actOptTypes;
	}

	//////////////////////////////////////////////////////////////////////

	private final void writeFacilitiesFile(ActivityFacilitiesImpl facilities, String actOptType, final String outdir) throws FileNotFoundException, IOException {
		BufferedWriter out = IOUtils.getBufferedWriter(outdir+"/facilities_"+actOptType+".txt.gz");
		out.write("ID\tX\tY\tCAPACITY\n");
		out.flush();
		for (ActivityFacility f : facilities.getFacilities().values()) {
			ActivityOption ao = f.getActivityOptions().get(actOptType);
			if (ao != null) {
				out.write(f.getId().toString()+"\t");
				out.write(f.getCoord().getX()+"\t");
				out.write(f.getCoord().getY()+"\t");
				out.write(ao.getCapacity()+"\n");
				out.flush();
			}
		}
		out.close();
	}
	
	//////////////////////////////////////////////////////////////////////
	// public methods
	//////////////////////////////////////////////////////////////////////

	public void run(ActivityFacilitiesImpl facilities, final String outdir) {
		log.info("running " + this.getClass().getName() + " module...");
		Set<String> actOptTypes = getActOptTypes(facilities);
		for (String actOptType : actOptTypes) {
			if (!actOptType.startsWith("B")) { // Ignoring NOGA types
				try { writeFacilitiesFile(facilities,actOptType,outdir); }
				catch (Exception e) { throw new RuntimeException(e); }
			}
		}
		log.info("done.");
	}
}
