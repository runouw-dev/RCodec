/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
