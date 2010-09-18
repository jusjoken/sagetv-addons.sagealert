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

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

/**
 * @author dbattams
 *
 */
public class ApiInterpreterTest extends TestCase {

	/**
	 * @param name
	 */
	public ApiInterpreterTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.google.code.sagetvaddons.sagealert.server.ApiInterpreter#interpret()}.
	 */
	@SuppressWarnings("unchecked")
	public final void testInterpret() {
		List<String> lines;
		try {
			lines = (List<String>)FileUtils.readLines(new File("api_tests.txt"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Object[] args = new Object[] {new Object()};
		for(int i = 0; i < lines.size(); ++i) {
			String test = lines.get(i).trim();
			if(test == null || test.length() == 0)
				continue;
			String expected = lines.get(++i);
			assertEquals(expected, new ApiInterpreter(args, test).interpret());
		}
	}
}
