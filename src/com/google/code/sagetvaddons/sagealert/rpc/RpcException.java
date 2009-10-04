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
 * General exception thrown when an RPC call fails
 * @author derek AT battams DOT ca
 * @version $Id$
 */
public final class RpcException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Throw an RpcException that is caused by another exception
	 * @param cause The underlying cause of this exception
	 */
	public RpcException(Throwable cause) {
		super(cause);
	}

	/**
	 * Throw an RpcException, with a custom message, that is caused by another exception
	 * @param message The custom message to attach to this exception
	 * @param cause The underlying cause of this exception
	 */
	public RpcException(String message, Throwable cause) {
		super(message, cause);
	}

}
