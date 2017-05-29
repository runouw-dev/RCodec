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

import com.runouw.rcodec.EncodeException;
import java.nio.charset.Charset;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public interface ByteWriter {

    default ByteWriter writeHeader() throws EncodeException {
        return write(new byte[] {'B', 'J', 'S', 'O', 'N'});
    }

    ByteWriter write(byte value) throws EncodeException;

    default ByteWriter write(char value) throws EncodeException {
        return write((byte) value);
    }

    default ByteWriter write(final byte[] value, final int offset, final int length) throws EncodeException {
        final int minIdx = offset;
        final int maxIdx = offset + length;

        if (minIdx < 0) {
            throw new IndexOutOfBoundsException("Index cannot be less than 0!");
        } else if (maxIdx > value.length) {
            throw new IndexOutOfBoundsException("Index cannot be greater than array length!");
        }

        for (int i = offset; i < (offset + length); i++) {
            write(value[i]);
        }

        return this;
    }

    default ByteWriter write(byte[] value) throws EncodeException {
        return write(value, 0, value.length);
    }

    default ByteWriter writeString(String str) throws EncodeException {
        return write(str.getBytes(Charset.forName("UTF8")));
    }

    default ByteWriter writeBoolean(boolean value) throws EncodeException {
        if (value) {
            write((byte) 0xFF);
        } else {
            write((byte) 0x00);
        }

        return this;
    }

    ByteWriter writeShort(short value) throws EncodeException;

    ByteWriter writeInt(int value) throws EncodeException;

    ByteWriter writeLong(long value) throws EncodeException;

    ByteWriter writeFloat(float value) throws EncodeException;

    ByteWriter writeDouble(double value) throws EncodeException;
}
