/*
 *      Copyright 2009 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * Access the list of registered event and handler classes
 * @author dbattams
 * @version $Id$
 */
final class RegisteredClasses {
	
	static private final Logger LOG = Logger.getLogger(RegisteredClasses.class);

	static private Collection<Class<?>> loadFromFile(File f) throws IOException {
		Collection<Class<?>> list = new ArrayList<Class<?>>();
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line;
		while((line = r.readLine()) != null) {
			line = line.trim();
			if(line.length() == 0 || line.startsWith("#"))
				continue;
			
			try {
				list.add(Class.forName(line));
			} catch(ClassNotFoundException e) {
				LOG.error("Registered class '" + line + "' not found!  Skipped.");
			}
		}
		r.close();
		return list;
	}
	
	/**
	 * Get the list of registered event classes; only registered classes can be fired
	 * @return The list of registered classes
	 * @throws IOException If the file of registered classes can't be found or is not accessible
	 */
	static Collection<Class<?>> getSageEventClasses() throws IOException {
		return loadFromFile(new File(AppInitializer.REGISTERED_EVENT_CLASSES));
	}
	
	/**
	 * Get the list of registered monitor classes; only registered classes will have threads spawned for them
	 * @return The list of registered classes
	 * @throws IOException If the file of registered classes can't be found or is not accessible
	 */
	static Collection<Class<?>> getSageMonitorClasses() throws IOException {
		return loadFromFile(new File(AppInitializer.REGISTERED_HANDLER_CLASSES));
	}
	
	private RegisteredClasses() {}
}
