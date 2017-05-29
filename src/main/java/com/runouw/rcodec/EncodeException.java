/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
