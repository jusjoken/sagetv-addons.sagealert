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
package com.google.code.sagetvaddons.sagealert.rpc;

/**
 * An exception that is thrown if the RPC client and the RPC server are not on the same version (i.e. they're incompatible)
 * @author derek AT battams DOT ca
 * @version $Id$
 */
public final class ApiVersionException extends Exception {
	private static final long serialVersionUID = -7362324940449652776L;

	/**
	 * Constructor to use when the server and client API versions do not match
	 * @param srvVersion The server's API version
	 * @param clntVersion The client's API version
	 */
	public ApiVersionException(int srvVersion, int clntVersion) {
		super("The server is running RPC API version '" + srvVersion + "', but the connected client is running RPC API version '" + clntVersion + "'");
	}
	
	/**
	 * Constructor; use to throw this exception with a customized error message
	 * @param msg The exception's custom message
	 */
	public ApiVersionException(String msg) {
		super(msg);
	}
}
