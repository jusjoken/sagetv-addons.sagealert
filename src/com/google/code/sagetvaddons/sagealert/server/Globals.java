/*
 *      Copyright 2010 Battams, Derek
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

import java.util.HashMap;
import java.util.Map;

import com.google.code.sagetvaddons.sagealert.server.globals.Date;
import com.google.code.sagetvaddons.sagealert.server.globals.SageTVServer;
import com.google.code.sagetvaddons.sagealert.server.globals.Utilities;

/**
 * @author dbattams
 *
 */
final class Globals {
	static private final Globals INSTANCE = new Globals();
	static final Globals get() { return INSTANCE; }
	
	private Map<String, Object> map;
	
	private Globals() {
		map = new HashMap<String, Object>();
		map.put("utils", new Utilities());
		map.put("srv", new SageTVServer());
	}
	
	public Object get(String key) {
		if(key.equals("date"))
			return new Date();
		return map.get(key);
	}
}
