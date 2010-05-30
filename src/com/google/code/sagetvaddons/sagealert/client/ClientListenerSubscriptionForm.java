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
package com.google.code.sagetvaddons.sagealert.client;

import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;

/**
 * @author dbattams
 *
 */
class ClientListenerSubscriptionForm extends ListenerSubscriptionForm {
	ClientListenerSubscriptionForm(Client clnt, EventType type) {
		super(new SageAlertEventMetadata(type.toString() + "_" + clnt.getId(), "Client Media Event (" + type.toString() + ")", "Fires when client '" + clnt.getAlias() + "' " + type.toString().toLowerCase() + " playing back media."));
	}
}
