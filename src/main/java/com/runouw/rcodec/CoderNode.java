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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An ordered Key -> Value map for encodable and decodable data.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public class CoderNode implements CoderContainer<String, CoderNode>{
    private final Map<String, CoderData> map = new HashMap<>();
    private final List<String> keysOrdered = new ArrayList<>();
    
    private CoderSettings settings = new CoderSettings();

    public CoderNode(CoderSettings settings) {
        this.settings = settings;
    }
    
    public CoderNode() {
    }
    
    public void setSettings(CoderSettings settings) {
        this.settings = settings;
    }

    @Override
    public CoderSettings getSettings() {
        return settings;
    }
    
    @Override
    public CoderNode self() {
        return this;
    }

    @Override
    public CoderData getCoderData(String key) {
        return map.get(key);
    }

    @Override
    public CoderNode setCoderData(String key, CoderData data) {
        map.put(key, data);
        keysOrdered.add(key);

        return this;
    }
    
    public List<String> getKeys() {
        return Collections.unmodifiableList(keysOrdered);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public JSONWriter write(JSONWriter writer) {
        writer.append('{');
        writer.setLevel(writer.getLevel() + 1);

        writer.putNewline(writer.getLevel());

        boolean useDelimiter = false;
        for(String key:keysOrdered){
            if(useDelimiter){
                writer.putDelimiter().putNewline(writer.getLevel());
            }

            writer.appendQuotedAddSlashes(key);
            writer.putColon();

            CoderData data = map.get(key);

            data.write(writer);

            useDelimiter = true;
        }

        writer.setLevel(writer.getLevel() - 1);
        writer.putNewline(writer.getLevel());
        writer.append('}');
        return writer;
    }

    @Override
    public ByteWriter write(ByteWriter writer) {
        writer.write((byte) '{');
        
        for(String key:keysOrdered){
            writer.writeString(key);
            writer.write((byte) '\0');

            CoderData data = map.get(key);
            data.write(writer);
        }

        
        // null string needed to signal an end to the stream of key value pairs
        writer.write((byte) '\0');
        writer.write((byte) '}');

        return writer;
    }

    @Override
    public CoderNode read(JSONReader reader, boolean readHeader) {
        if(readHeader && reader.getNextToken().type != JSONTokenizer.TokenType.START_BRACE){
            throw new DecodeException("Start token does not match!");
        }
        
        boolean expectComma = false;
        while(true){
            JSONTokenizer.Token token = reader.getNextToken();
            switch (token.type) {
                case COMMA:
                    if(!expectComma){
                        throw new DecodeException("Unexpected token " + token);
                    }
                    expectComma = false;
                    
                    break;
                case END_BRACE:
                    return this;
                case EOF:
                    throw new DecodeException("End of file reached!");
                case STRING:
                    String key = token.buf;

                    JSONTokenizer.Token colonToken = reader.getNextToken();
                    if(colonToken.type != JSONTokenizer.TokenType.COLON){
                        throw new DecodeException("Unexpected token (expecting ':') " + colonToken);
                    }

                    JSONTokenizer.Token valueToken = reader.getNextToken();
                    
                    CoderData data = CoderData.fromJSONToken(valueToken, reader);
                    map.put(key, data);
                    keysOrdered.add(key);
                    
                    expectComma = true;
                    break;
                default:
                    throw new DecodeException("Unexpected token " + token);
            }
        }
    }
    
    @Override
    public CoderNode read(ByteReader reader, boolean readHeader) {
        if(readHeader && reader.readByte() != '{'){
            throw new DecodeException("Byte header for CoderNode not found!");
        }
        
        while(true){
            String key = reader.readString();
            
            byte c = reader.readByte();
            if(c == '}'){
                return this;
            }
            CoderData.Type type = CoderData.Type.fromByte(c)
                    .orElseThrow(() -> new DecodeException("ByteReader character not supported! " + c));
            CoderData data = type.decode(reader);
            
            map.put(key, data);
            keysOrdered.add(key);
        }
    }
    
    /*
    public static CoderNode from(byte[] bytes){
        ByteReaderByteArray reader = new ByteReaderByteArray(bytes);
        if(reader.checkHeader()){
            return new CoderNode().read(reader);
        }else{
            JSONReader jsonReader = new JSONReaderString(bytes);
            
            return new CoderNode().read(jsonReader, true);
        }
    }
    
    public static CoderNode from(String str){
        JSONReader jsonReader = new JSONReaderString(str);
            
        return new CoderNode().read(jsonReader, true);
    }
    */

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.map);
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
        final CoderNode other = (CoderNode) obj;
        
        return Objects.equals(this.map.entrySet(), other.map.entrySet());
    }
    
    
}
