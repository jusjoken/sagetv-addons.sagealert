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

import gkusnick.sagetv.api.API;

import java.io.File;

/**
 * @author dbattams
 *
 */
final class Utils {
	static private final String MEDIA_FILE_SIG = "MediaFile[";
	
	static boolean canWrapAsMediaFile(Object obj) {
		return obj != null && (API.apiNullUI.mediaFileAPI.IsMediaFileObject(obj) ||
								(!(obj instanceof String) && obj.toString().startsWith(MEDIA_FILE_SIG)) ||
								(obj instanceof File && API.apiNullUI.mediaFileAPI.GetMediaFileForFilePath((File)obj) != null) ||
								(API.apiNullUI.airingAPI.IsAiringObject(obj) && API.apiNullUI.airingAPI.Wrap(obj).GetMediaFileForAiring() != null));
	}
	
	static boolean containsNonEmptyString(Object[] objs) {
		for(Object o : objs)
			if(o != null && o instanceof String && o.toString().length() > 0)
				return true;
		return false;
	}
	
	private Utils() {}
}
