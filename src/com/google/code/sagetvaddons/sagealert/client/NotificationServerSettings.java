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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * All notification systems are initiated by an object that implements this interface.
 * 
 * Basically, notification server settings must be transferable between the client (browser) and the SageAlert server so it must implement IsSerializable (see GWT docs 
 * for details on how to ensure that a class in indeed serializable in the GWT sense).  And these objects are also stored in the SageAlert data store so they must also
 * implement IsDataStoreSerializable.
 * 
 * @author dbattams
 * @version $Id$
 */
public interface NotificationServerSettings extends IsDataStoreSerializable, IsSerializable {
	// Marker for settings objects
}
