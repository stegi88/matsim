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

/**
 * 
 */
package playground.johannes.gsv.visum;

import org.matsim.api.core.v01.Id;

/**
 * @author johannes
 *
 */
public class PrefixIdGenerator implements IdGenerator {

	private final String prefix;
	
	public PrefixIdGenerator(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public <T> Id<T> generateId(String str, Class<T> idType) {
		return Id.create(prefix + str, idType);
	}

}
