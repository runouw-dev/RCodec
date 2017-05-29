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

import java.io.StringWriter;

/**
 *
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
public interface JSONWriter {

    BeautifyRules getRules();

    int getLevel();

    void setLevel(int level);

    default JSONWriter append(boolean value) {
        append(value ? "true" : "false");
        return this;
    }

    default JSONWriter append(byte value) {
        append((int) value);

        return this;
    }

    default JSONWriter append(short value) {
        append((int) value);

        return this;
    }

    JSONWriter append(int value);

    JSONWriter append(long value);

    JSONWriter append(float value);

    JSONWriter append(double value);

    JSONWriter append(char value);

    JSONWriter append(CharSequence value);

    default JSONWriter appendQuotedAddSlashes(String value) {
        append('"');
        append(JSONUtils.cleanString(value));
        append('"');

        return this;
    }

    default JSONWriter putColon() {
        switch (getRules().getIndentation()) {
            case SPACES:
            case TABS:
                append(": ");
                break;
            case MINIFIED:
                append(":");
                break;
        }
        return this;
    }

    default public JSONWriter putDelimiter() {
        switch (getRules().getIndentation()) {
            case SPACES:
            case TABS:
                append(", ");
                break;
            case MINIFIED:
                append(",");
                break;
        }
        return this;
    }

    default JSONWriter putNewline(int level) {
        switch (getRules().getIndentation()) {
            case SPACES:
            case TABS:
                append('\r');
                append('\n');
                break;
        }

        putSpacing(level);

        return this;
    }

    default String getNewLine(int level) {
        final BeautifyRules rules = getRules();
        final StringWriter writer = new StringWriter(2 + level * rules.getSpacing());

        switch (rules.getIndentation()) {
            case SPACES:
                writer.append('\r');
                writer.append('\n');
                for (int i = 0; i < level * rules.getSpacing(); i++) {
                    writer.append(' ');
                }
                break;
            case TABS:
                writer.append('\r');
                writer.append('\n');
                for (int i = 0; i < level * rules.getSpacing(); i++) {
                    writer.append('\t');
                }
                break;
        }

        return writer.toString();
    }

    default JSONWriter putSpacing(int level) {
        final BeautifyRules rules = getRules();

        switch (rules.getIndentation()) {
            case SPACES:
                for (int i = 0; i < level * rules.getSpacing(); i++) {
                    append(' ');
                }
                break;
            case TABS:
                for (int i = 0; i < level * rules.getSpacing(); i++) {
                    append('\t');
                }
                break;
        }

        return this;
    }
}
