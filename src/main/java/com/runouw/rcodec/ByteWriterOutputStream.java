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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * An implementation of ByteWriter that writes to an OutputStream.
 *
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 * @param <BaseStreamT> the type of base stream
 */
class ByteWriterOutputStream<BaseStreamT extends OutputStream> implements ByteWriter, AutoCloseable {

    private final BaseStreamT out;
    private final DataOutputStream stream;

    /**
     * Retrieves the internal base stream.
     *
     * @return the internal base stream
     */
    protected final BaseStreamT getStream() {
        return out;
    }

    /**
     * Constructs a new ByteWriterOutputStream by wrapping the supplied
     * OutputStream.
     *
     * @param out the stream to write to.
     */
    public ByteWriterOutputStream(final BaseStreamT out) {
        this.out = Objects.requireNonNull(out);
        this.stream = new DataOutputStream(out);
    }

    @Override
    public ByteWriter write(byte value) throws EncodeException {
        try {
            this.stream.writeByte(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write byte to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public ByteWriter write(final char value) throws EncodeException {
        write((byte) value);

        return this;
    }

    @Override
    public ByteWriter write(final byte[] arr) throws EncodeException {
        try {
            this.stream.write(arr);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write byte array to OutputStream!", ex);
        }

        return this;
    }
    
    @Override
    public ByteWriter write(final byte[] arr, final int off, final int len) throws EncodeException {
        try {
            this.stream.write(arr, off, len);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write byte array segment to OutputStream!", ex);
        }
        
        return this;
    }

    @Override
    public ByteWriter writeShort(short value) throws EncodeException {
        try {
            stream.writeShort(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write short to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public ByteWriter writeInt(int value) throws EncodeException {
        try {
            stream.writeInt(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write int to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public ByteWriter writeLong(long value) throws EncodeException {
        try {
            stream.writeLong(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write long to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public ByteWriter writeFloat(float value) throws EncodeException {
        try {
            stream.writeFloat(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write float to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public ByteWriter writeDouble(double value) throws EncodeException {
        try {
            stream.writeDouble(value);
        } catch (IOException ex) {
            throw new EncodeException("Unable to write double to OutputStream!", ex);
        }

        return this;
    }

    @Override
    public void close() throws IOException {
        this.stream.close(); //probably not necessary
        this.out.close();
    }
}
