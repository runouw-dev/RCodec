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

import com.runouw.rcodec.DecodeException;
import java.util.Arrays;

/**
 * A reader interface for interacting with data encoded as BJSON.
 *
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
public interface ByteReader {

    /**
     * Attempts to read a boolean value from the BJSON.
     *
     * @return the value as boolean
     * @throws DecodeException if the value could not be read.
     */
    default boolean readBoolean() throws DecodeException {
        return readByte() != 0x00;
    }

    /**
     * Attempts to read a byte value from the BJSON
     *
     * @return the value as a 8bit signed integer.
     * @throws DecodeException if the value could not be read.
     */
    byte readByte() throws DecodeException;

    /**
     * Attempts to read a short value from the BJSON.
     *
     * @return the value as a 16bit signed integer.
     * @throws DecodeException if the value could not be read.
     */
    short readShort() throws DecodeException;

    /**
     * Attempts to read an int value from the BJSON.
     *
     * @return the value as a 32bit signed integer.
     * @throws DecodeException if the value could not be read.
     */
    int readInt() throws DecodeException;

    /**
     * Attempts to read a long value from the BJSON.
     *
     * @return the value as a 64bit signed integer.
     * @throws DecodeException if the value could not be read.
     */
    long readLong() throws DecodeException;

    /**
     * Attempts to read a float value from the BJSON.
     *
     * @return the value as a 32bit IEEE754 float.
     * @throws DecodeException if the value could not be read.
     */
    float readFloat() throws DecodeException;

    /**
     * Attempts to read a double value from the BJSON.
     *
     * @return the value as a 64bit IEEE754 double.
     * @throws DecodeException if the value could not be read.
     */
    double readDouble() throws DecodeException;

    /**
     * Attempts to read a String value from the BJSON.
     *
     * @return the value as a String.
     * @throws DecodeException if the value could not be read.
     */
    String readString() throws DecodeException;

    /**
     * Checks the header. This will read the next 5 bytes and check if they
     * decode to the BJSON header. This is only valid at the beginning of
     * parsing BJSON.
     *
     * @return true if the next 5 bytes encode a BJSON header.
     * @throws DecodeException if the value could not be read.
     */
    default boolean checkHeader() throws DecodeException {
        final byte[] data = this.readBytes(5);

        return Arrays.equals(data, new byte[]{'B', 'J', 'S', 'O', 'N'});
    }

    /**
     * Bulk read operation. The default implementation for this populates a byte
     * array of the specified length and iterates across it, filling the data
     * with readByte(). Other implementations may use an optimized form.
     *
     * @param length the number of bytes to read.
     * @return the byte array of size length
     * @throws DecodeException if the value could not be read
     */
    default byte[] readBytes(int length) throws DecodeException {
        final byte[] out = new byte[length];

        for (int i = 0; i < length; i++) {
            out[i] = readByte();
        }

        return out;
    }

}
