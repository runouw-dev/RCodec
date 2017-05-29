/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
