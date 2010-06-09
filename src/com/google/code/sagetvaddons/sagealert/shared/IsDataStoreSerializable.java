/*
 *      Copyright 2009-2010 Battams, Derek
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
package com.google.code.sagetvaddons.sagealert.shared;

/**
 * <p>Denotes that an object can be serialized for storage in the data store.</p>
 * 
 * <p>Though not definable in the interface, all implementers must also provide an
 * empty ctor and a method with signature: <code>void unserialize(String key, String data)</code></p>
 * <p>The unserialize method should be protected or private since it should only be used by the reflection code.</p>
 * to reconstruct objects from the data store
 * @author dbattams
 * @version $Id$
 */
public interface IsDataStoreSerializable {
	/**
	 * <p>A serialization of the unique value(s) that identify an instance.</p>
	 * 
	 * <p>If that value is just one field (i.e. a user id) then this method should just return that single value.  If, however, that value is a composite of many fields then
	 * this method must return a single string that packs all of those fields into one value.  The technique used to generate this string must be reversed when the unserialize()
	 * method is called.</p>
	 *  
	 * @return The unique identifier suitable for storage in the app data store
	 */
	public String getDataStoreKey();
	
	/**
	 * <p>A serialization of the non-unique data value(s) for an instance.</p>
	 * 
	 * <p>This value should pack all of those state values that were not included in the getDataStoreKey() method.  If there is only one field then this value should just return
	 * that field.  If, however, there is more than one field then this string must pack all of those fields into one value.  The technique used to generate this string must
	 * be reversed when the unserialize() method is called in order to restore the state of the a given instance.</p>
	 * 
	 * @return The serialization of all remaining state fields not included in the key value of the instance
	 */
	public String getDataStoreData();
	
	/**
	 * <p>The deserialization method for an object retrieved from the data store.</p>
	 * 
	 * <p>This method takes the packed key and data values from the data store and restores the state of the object based on the values received.</p>
	 * @param key The key value(s) for this object
	 * @param data The data value(s) for this object
	 */
	public void unserialize(String key, String data);
}
