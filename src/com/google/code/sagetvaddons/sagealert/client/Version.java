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

/**
 * Stores build version info; version numbers are injected at build time
 * @author dbattams
 * @version $Id$
 */
final class Version {
	final static String getVersion() {
		return "@@VER_NUM@@";
	}
	
	final static String getBuild() {
		return "@@BLD_NUM@@";
	}
	
	final static String getFullVersion() {
		return getVersion() + "." + getBuild();
	}
}
