/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.util.Objects;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class JSONWriterStringBuilder implements JSONWriter{
    private final StringBuilder builder;
    private int level;
    private final BeautifyRules rules;

    public JSONWriterStringBuilder() {
        this(BeautifyRules.SPACES_X_4);
    }

    public JSONWriterStringBuilder(BeautifyRules rules) {        
        this.builder = new StringBuilder();
        this.rules = Objects.requireNonNull(rules);
    }

    /*
    public JSONWriterStringBuilder(StringBuilder out, BeautifyRules rules) {
        super(rules);
        this.builder = out;
    }
    */

    public StringBuilder getBuilder() {
        return builder;
    }

    @Override
    public JSONWriter append(int value){
        builder.append(value);
        return this;
    }

    @Override
    public JSONWriter append(long value){
        builder.append(value);
        return this;
    }

    @Override
    public JSONWriter append(float value){
        builder.append(value);
        return this;
    }

    @Override
    public JSONWriter append(double value){
        builder.append(value);
        return this;
    }

    @Override
    public JSONWriter append(char value){
        builder.append(value);
        return this;
    }

    @Override
    public JSONWriter append(CharSequence value){
        builder.append(value);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
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
