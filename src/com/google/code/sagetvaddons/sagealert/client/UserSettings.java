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
 * List of user configurable settings and their default values
 * @author dbattams
 * @version $Id$
 */
public interface UserSettings {
	
	/**
	 * Time display settings; string value to be passed to SimpleDateFormat class
	 */
	public final String TIME_FORMAT_SHORT  = "TimeFormatShort";
	public final String TIME_FORMAT_SHORT_DEFAULT = "H:mm";
	
	/**
	 * Time display settings; string value to be passed to SimpleDateFormat class
	 */	
	public final String TIME_FORMAT_MEDIUM = "TimeFormatMed";
	public final String TIME_FORMAT_MEDIUM_DEFAULT = "H:mm z";
	
	/**
	 * Time display settings; string value to be passed to SimpleDateFormat class
	 */
	public final String TIME_FORMAT_LONG   = "TimeFormatLong";
	public final String TIME_FORMAT_LONG_DEFAULT = "H:mm z";
	
	/**
	 * The amount of time, in ms, between each run of the recording monitor
	 */
	public final String REC_MONITOR_SLEEP = "RecMonSleep";
	public final String REC_MONITOR_SLEEP_DEFAULT = "60000";
	
	/**
	 * Amount of time, in ms, between each run of the UI monitor
	 */
	public final String UI_MONITOR_SLEEP = "UiMonSleep";
	public final String UI_MONITOR_SLEEP_DEFAULT = "300000";
	
	/**
	 * Amount of time, in ms, between each run of the recording conflict monitor
	 */
	public final String CONFLICT_MONITOR_SLEEP = "ConflictMonSleep";
	public final String CONFLICT_MONITOR_SLEEP_DEFAULT = "300000";
	
	/**
	 * Amount of time, in ms, before a repeat conflict event is fired
	 */
	public final String CONFLICT_REPORT_DELAY = "ConflictReportDelay";
	public final String CONFLICT_REPORT_DELAY_DEFAULT = "14400000";
	
	/**
	 * Amount of time, in ms, between each run of the system message monitor
	 */
	public final String SYSMSG_MONITOR_SLEEP = "SysMsgMonSleep";
	public final String SYSMSG_MONITOR_SLEEP_DEFAULT = "60000";
	
	/**
	 * Amount of time, in ms, between each run of the low space monitor
	 */
	public final String LOW_SPACE_MONITOR_SLEEP = "LowSpaceMonSleep";
	public final String LOW_SPACE_MONITOR_SLEEP_DEFAULT = "300000";
	
	/**
	 * The number of GBs free below which a low space event will be fired
	 */
	public final String LOW_SPACE_THRESHOLD = "LowSpaceThreshold";
	public final String LOW_SPACE_THRESHOLD_DEFAULT = "20";

	/**
	 * Amount of time, in ms, between each run of the viewing client monitor
	 */
	public final String VIEWING_CLNT_MONITOR_SLEEP = "ViewingClntMonSleep";
	public final String VIEWING_CLNT_MONITOR_SLEEP_DEFAULT = "120000"; 
}
