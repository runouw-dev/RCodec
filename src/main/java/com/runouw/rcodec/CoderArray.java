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
import com.runouw.rcodec.JSONTokenizer.Token;
import com.runouw.rcodec.JSONTokenizer.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An object that represents a JSON/BJSON array.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
public final class CoderArray implements CoderContainer<Integer, CoderArray> {

    private final List<CoderData> arr = new ArrayList<>();
    private CoderSettings settings = new CoderSettings();
    
    public CoderArray(CoderSettings settings) {
        this.settings = settings;
    }

    public CoderArray() {
    }

    public void setSerializer(CoderSettings settings) {
        this.settings = settings;
    }

    @Override
    public CoderSettings getSettings() {
        return settings;
    }

    @Override
    public CoderArray self() {
        return this;
    }

    //NOTE: Object type allows you to place "null" types with null checking
    //NOTE: As well as allows support for CoderSerializer to try encoding it
    public CoderArray add(Object value) {
        return set(size(), value);
    }

    @Override
    public CoderData getCoderData(Integer key) {
        if (key >= arr.size()) {
            return null;
        }
        return arr.get(key);
    }

    @Override
    public CoderArray setCoderData(Integer key, CoderData data) {
        if (key == arr.size()) {
            arr.add(data);
        } else {
            arr.set(key, data);
        }
        return this;
    }

    public CoderArray addNode(Consumer<CoderNode> onNode) {
        return withNode(arr.size(), onNode);
    }

    public CoderArray addArray(Consumer<CoderArray> onNode) {
        return withArray(arr.size(), onNode);
    }

    public int size() {
        return arr.size();
    }

    @SuppressWarnings("unchecked")
    public <T> void forEach(Class<T> cl, Consumer<T> consumer) {
        for (int i = 0; i < arr.size(); i++) {
            consumer.accept((T) get(i, cl));
        }
    }

    @Override
    public String toString() {
        return asString();
    }
    
    private static final char JSON_ARRAY_HEADER_BEGIN = '[';
    private static final char JSON_ARRAY_HEADER_END = ']';

    @Override
    public JSONWriter write(JSONWriter writer) {
        if (writer.getRules().getIndentation() == Indentation.MINIFIED) {

            writer.append(JSON_ARRAY_HEADER_BEGIN);
            writer.setLevel(writer.getLevel() + 1);

            boolean useDelimiter = false;
            boolean brokeFirstLine = false;

            for (CoderData data : arr) {
                if (useDelimiter) {
                    writer.putDelimiter();
                }
                data.write(writer);

                useDelimiter = true;
            }

            writer.setLevel(writer.getLevel() - 1);

            if (brokeFirstLine) {
                writer.putNewline(writer.getLevel());
            }
            
            writer.append(JSON_ARRAY_HEADER_END);
        } else {
            if (writer instanceof JSONWriterStringBuilder) {
                writeWithLineBreaks((JSONWriterStringBuilder) writer);
            } else {
                JSONWriterStringBuilder b = new JSONWriterStringBuilder(writer.getRules());
                writeWithLineBreaks(b);
                writer.append(b.toString());
            }
        }

        return writer;
    }

    private JSONWriter writeWithLineBreaks(JSONWriterStringBuilder writer) {
        writer.append(JSON_ARRAY_HEADER_BEGIN);
        writer.setLevel(writer.getLevel() + 1);

        int leftBracket = writer.getBuilder().length();

        // writer.putNewline(writer.getLevel());
        boolean useDelimiter = false;
        int lastPos = writer.getBuilder().length();

        boolean brokeFirstLine = false;

        for (CoderData data : arr) {
            if (useDelimiter) {
                writer.putDelimiter();

                final int pos = writer.getBuilder().length();

                if (pos - lastPos > writer.getRules().getArrayWrappingLength()
                        && writer.getRules().getIndentation() != Indentation.MINIFIED) {
                    
                    writer.putNewline(writer.getLevel());

                    if (brokeFirstLine == false) {
                        writer.getBuilder().insert(leftBracket, writer.getNewLine(writer.getLevel()));
                        brokeFirstLine = true;
                    }
                    
                    lastPos = writer.getBuilder().length();
                }
            }
            
            data.write(writer);
            useDelimiter = true;
        }

        writer.setLevel(writer.getLevel() - 1);

        if (brokeFirstLine) {
            writer.putNewline(writer.getLevel());
        }
        
        writer.append(JSON_ARRAY_HEADER_END);

        return writer;
    }

    private static final byte BJSON_ARRAY_HEADER_BEGIN = (byte) '[';
    private static final byte BJSON_ARRAY_HEADER_END = (byte) ']';
    
    @Override
    public ByteWriter write(ByteWriter writer) {
        writer.write(BJSON_ARRAY_HEADER_BEGIN);

        for (CoderData data : arr) {
            data.write(writer);
        }

        writer.write(BJSON_ARRAY_HEADER_END);

        return writer;
    }

    @Override
    public CoderArray read(ByteReader reader, boolean readHeader) {
        if (readHeader && reader.readByte() != BJSON_ARRAY_HEADER_BEGIN) {
            throw new DecodeException("Byte header for CoderNode not found!");
        }

        while (true) {
            final byte c = reader.readByte();

            if (c == BJSON_ARRAY_HEADER_END) {
                return this;
            }

            final CoderData.Type type = CoderData.Type.fromByte(c)
                    .orElseThrow(() -> new DecodeException("ByteReader character not supported! " + c));
            
            final CoderData data = type.decode(reader);

            arr.add(data);
        }
    }

    @Override
    public CoderArray read(final JSONReader reader, final boolean readHeader) {
        if (readHeader && reader.getNextToken().type != TokenType.START_BRACKET) {
            throw new DecodeException("Start token does not match!");
        }

        boolean expectComma = false;
        while (true) {
            Token token = reader.getNextToken();
            switch (token.type) {
                case COMMA:
                    if (!expectComma) {
                        throw new DecodeException("Unexpected token " + token);
                    }
                    
                    expectComma = false;

                    break;
                case END_BRACKET:
                    return this;
                case EOF:
                    throw new DecodeException("End of file reached!");
                default:
                    final CoderData data = CoderData.fromJSONToken(token, reader);
                    
                    arr.add(data);
                    expectComma = true;
                    break;
            }
        }
    }

    /*
    public static CoderArray from(byte[] bytes){
        ByteReaderByteArray reader = new ByteReaderByteArray(bytes);
        if(reader.checkHeader()){
            return new CoderArray().read(reader);
        }else{
            JSONReader jsonReader = new JSONReaderString(bytes);
            
            return new CoderArray().read(jsonReader, true);
        }
    }
    
    public static CoderArray from(String str){
        JSONReader jsonReader = new JSONReaderString(str);
            
        return new CoderArray().read(jsonReader, true);
    }
    
    public static CoderArray from(Path path) throws IOException{
        try(JSONReaderInputStream reader = new JSONReaderInputStream(Files.newInputStream(path))){
            return new CoderArray().read(reader);
        }catch(Exception ex){
            throw new IOException(ex);
        }
    }
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.arr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoderArray other = (CoderArray) obj;

        return Objects.equals(this.arr, other.arr);
    }

}
