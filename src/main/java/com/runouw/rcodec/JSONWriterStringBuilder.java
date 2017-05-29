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
