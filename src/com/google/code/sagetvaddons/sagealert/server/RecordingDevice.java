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

/**
 * An abstract recording device used to relay information about the source/cause of a LowSpaceEvent
 * @author dbattams
 * @version $Id$
 */
public class RecordingDevice {
	private long freeSpace;
	private long minSpace;
	
	/**
	 * Constructor
	 * @param freeSpace The amount of free space on this device, in bytes
	 * @param minSpace The minimum amount of space required on this device, in bytes
	 */
	public RecordingDevice(long freeSpace, long minSpace) {
		this.freeSpace = freeSpace / 1024 / 1024 / 1024;
		this.minSpace = minSpace / 1024 / 1024/ 1024;
	}

	/**
	 * @return the freeSpace (coverted to GBs)
	 */
	public long getFreeSpace() {
		return freeSpace;
	}

	/**
	 * @return the minSpace (converted to GBs)
	 */
	public long getMinSpace() {
		return minSpace;
	}
}
