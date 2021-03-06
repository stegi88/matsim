/* *********************************************************************** *
 * project: org.matsim.*
 * AnalyzeSeed2SeedAPL.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2011 by the members listed in the COPYING,        *
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
package playground.johannes.studies.snowball;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.matsim.contrib.socnetgen.sna.graph.SparseEdge;
import org.matsim.contrib.socnetgen.sna.graph.SparseGraph;
import org.matsim.contrib.socnetgen.sna.graph.SparseVertex;
import org.matsim.contrib.socnetgen.sna.graph.analysis.GraphAnalyzer;
import org.matsim.contrib.socnetgen.sna.graph.io.SparseGraphMLReader;
import org.matsim.contrib.socnetgen.sna.snowball.SampledGraphProjection;
import org.matsim.contrib.socnetgen.sna.snowball.analysis.SeedAPLTask;
import org.matsim.contrib.socnetgen.sna.snowball.io.SampledGraphProjMLReader;

import java.io.IOException;
import java.util.Map;

/**
 * @author illenberger
 * 
 */
public class AnalyzeSeed2SeedAPL {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		SampledGraphProjMLReader<SparseGraph, SparseVertex, SparseEdge> reader = new SampledGraphProjMLReader<SparseGraph, SparseVertex, SparseEdge>(new SparseGraphMLReader());
		SampledGraphProjection<SparseGraph, SparseVertex, SparseEdge> graph = reader.readGraph("");

		Map<String, DescriptiveStatistics> map = GraphAnalyzer.analyze(graph, new SeedAPLTask());
		GraphAnalyzer.writeStatistics(map, "", true);
	}

}
