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
