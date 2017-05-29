/* 
 * Copyright 2017 Robert Hewitt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.runouw.rcodec;

/**
 * An implementation of RuntimeException that is thrown when an exception occurs
 * while decoding an object.
 *
 * @author Robert
 * @since 15.07.13
 */
@SuppressWarnings("serial")
public class DecodeException extends RuntimeException {

    /**
     * Constructs a new DecodeException with null as its detail message.
     *
     * @since 15.07.13
     */
    public DecodeException() {
        super();
    }

    /**
     * Constructs a new DecodeException with the specified detail message.
     *
     * @param msg the detail message
     * @since 15.07.13
     */
    public DecodeException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new DecodeException with the specified cause and a detail
     * message of either null or the String form of cause.
     *
     * @param cause the cause of the exception.
     * @since 15.07.13
     */
    public DecodeException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DecodeException with the specified detail message and
     * cause.
     *
     * @param msg the detail message
     * @param cause the cause.
     * @since 15.07.13
     */
    public DecodeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
