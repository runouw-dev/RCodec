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
 * An Exception that is thrown if unexpected behavior is caught while encoding
 * an object.
 *
 * @author Robert
 * @since 16.08.24
 */
@SuppressWarnings("serial")
public class EncodeException extends RuntimeException {

    /**
     * Constructs a new EncodeException with no message or cause.
     *
     * @since 16.08.24
     */
    public EncodeException() {
        super();
    }

    /**
     * Constructs a new EncodeException with a message.
     *
     * @param msg the message explaining why this exception occurred.
     * @since 16.08.24
     */
    public EncodeException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new EncodeException with a cause.
     *
     * @param cause the exception that caused this exception to be thrown.
     * @since 16.08.24
     */
    public EncodeException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new EncodeException with a message and cause.
     *
     * @param msg the message that explains why this exception occurred.
     * @param cause the exception that caused this exception to be thrown.
     * @since 16.08.24
     */
    public EncodeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
