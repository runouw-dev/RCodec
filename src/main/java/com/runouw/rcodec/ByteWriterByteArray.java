/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.io.ByteArrayOutputStream;

/**
 * An implementation of ByteWriter that writes to a byte array.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class ByteWriterByteArray extends ByteWriterOutputStream<ByteArrayOutputStream> {    
    private static final int DEFAULT_INTERNAL_INITIAL_BUFFER_SIZE = Integer.getInteger("com.runouw.rcodec2.ByteWriterByteArray.default_internal_initial_buffer_size_", 4096);
    
    /**
     * Constructs a new ByteWriterByteArray using the default initial internal buffer size.
     */
    public ByteWriterByteArray() {
        this(DEFAULT_INTERNAL_INITIAL_BUFFER_SIZE);
    }
    
    /**
     * Constructs a new ByteWriterByteArray using the specified initial internal buffer size.
     * @param initialSize the initial internal buffer size.
     */
    public ByteWriterByteArray(final int initialSize) {
        super(new ByteArrayOutputStream(initialSize));
    }        

    /**
     * Retrieves the internal byte array.
     * @return the byte array.
     */
    public byte[] toBytes(){
        return this.getStream().toByteArray();
    }

}
