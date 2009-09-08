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
package com.google.code.sagetvaddons.sagealert.client;

import com.google.code.gwtsrwc.client.ValidatedIntegerTextBox;

/**
 * A ValidatedIntegerTextBox that converts its input to ms
 * @author dbattams
 * @version $Id$
 */
class ValidatedMinutesTextBox extends ValidatedIntegerTextBox {
	/**
	 * Constructor
	 * @param min Minimum value
	 * @param max Max value
	 */
	ValidatedMinutesTextBox(int min, int max) {
		super(min, max);
	}

	@Override
	public String getValue() {
		return Long.toString(60000L * Integer.parseInt(super.getValue()));
	}
	
	@Override
	public void setValue(String val) {
		try {
			int num = Integer.parseInt(val);
			val = Integer.toString(num / 60000);
		} catch(NumberFormatException e) {
			// Ignore it
		}
		super.setValue(val);
	}
}
