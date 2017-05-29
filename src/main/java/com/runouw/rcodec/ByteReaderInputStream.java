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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class ByteReaderInputStream implements ByteReader, AutoCloseable{
    private final DataInputStream in;

    public ByteReaderInputStream(final InputStream in) {
        this.in = new DataInputStream(in);
    }
    
    @Override
    public byte readByte() {
        try{
            return in.readByte();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public short readShort() {
        try{
            return in.readShort();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public int readInt() {
        try{
            return in.readInt();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public long readLong() {
        try{
            return in.readLong();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public float readFloat() {
        try{
            return in.readFloat();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public double readDouble() {
        try{
            return in.readDouble();
        }catch(IOException ex){
            throw new Error(ex);
        }
    }

    @Override
    public String readString() {
        try (ByteArrayOutputStream buildString = new ByteArrayOutputStream()) {
            while(true){
                char c = (char) in.read();
                if (c == '\0') {
                    break;
                } else {
                    buildString.write(c);
                }
            }
            return new String(buildString.toByteArray(), "UTF8");
        }catch(IOException ex){
            throw new Error(ex);
        }
    }
    
    @Override
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        try{
            in.readFully(bytes);
        }catch(IOException ex){
            throw new Error(ex);
        }
        return bytes;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
