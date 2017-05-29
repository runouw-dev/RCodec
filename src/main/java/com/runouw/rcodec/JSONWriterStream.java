/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class JSONWriterStream implements JSONWriter, AutoCloseable {    
    private final OutputStreamWriter bout;
    private final BeautifyRules rules;
    private int level;

    public JSONWriterStream(final OutputStream out) {
        this(out, BeautifyRules.SPACES_X_4);        
    }

    public JSONWriterStream(OutputStream out, BeautifyRules rules) {        
        this.bout = new OutputStreamWriter(out, Charset.forName("UTF8"));
        this.rules = Objects.requireNonNull(rules);
    }

    @Override
    public JSONWriter append(int value) {
        try{
            bout.append(String.valueOf(value));
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public JSONWriter append(long value) {
        try{
            bout.append(String.valueOf(value));
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public JSONWriter append(float value) {
        try{
            bout.append(String.valueOf(value));
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public JSONWriter append(double value) {
        try{
            bout.append(String.valueOf(value));
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public JSONWriter append(char value) {
        try{
            bout.append(value);
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public JSONWriter append(CharSequence value) {
        try{
            bout.append(value);
            bout.flush();
        }catch(IOException ex){
            throw new Error(ex);
        }
        return this;
    }

    @Override
    public void close() throws IOException {
        bout.close();
    }

    @Override
    public BeautifyRules getRules() {
        return this.rules;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

}
