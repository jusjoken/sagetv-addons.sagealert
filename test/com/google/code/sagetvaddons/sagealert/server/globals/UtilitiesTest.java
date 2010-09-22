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
package com.google.code.sagetvaddons.sagealert.server.globals;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dbattams
 *
 */
public class UtilitiesTest {

	private Utilities utils;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		utils = new Utilities();
	}

	/**
	 * Test method for {@link com.google.code.sagetvaddons.sagealert.server.globals.Utilities#bytesToString(long, java.lang.String)}.
	 */
	@Test
	public final void bytesToStringTest() {
		long oneTB = FileUtils.ONE_GB * 1024L;
		
		assertEquals("1.00 GB", utils.bytesToString(FileUtils.ONE_GB, "G"));
		assertEquals("1.01 TB", utils.bytesToString(oneTB + oneTB / 100, "G"));
		assertEquals("332.08 MB", utils.bytesToString(332 * FileUtils.ONE_MB + FileUtils.ONE_MB / 100 * 8, "B"));
		assertEquals("32.48 KB", utils.bytesToString(32 * FileUtils.ONE_KB + FileUtils.ONE_KB / 100 * 48 + 10, "K"));
		assertEquals("833 bytes", utils.bytesToString(833, "B"));
		assertEquals("0.24 TB", utils.bytesToString(243L * FileUtils.ONE_GB, "T"));
	}
}
