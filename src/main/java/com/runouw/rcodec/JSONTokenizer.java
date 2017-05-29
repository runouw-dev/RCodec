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
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author Robert
 */
class JSONTokenizer {
    private final Reader in;
    private int charLoc = 0;
    private int lineLoc = 0;
    
    private Token readyToken = null;
    
    public JSONTokenizer(Reader in) {
        this.in = in;
    }
    public Token nextToken(){
        if(readyToken != null){
            Token t = readyToken;
            readyToken = null;
            return t;
        }
        
        StringBuilder buff = new StringBuilder();

        try {
            while(true){
                int read = in.read();
                charLoc++;
                if(read == -1){
                    readyToken = makeToken(TokenType.EOF);
                }

                char c = (char) read;

                switch(c){
                    case '{':
                        readyToken = makeToken(TokenType.START_BRACE);
                        break;
                    case '}':
                        readyToken = makeToken(TokenType.END_BRACE);
                        break;
                    case '[':
                        readyToken = makeToken(TokenType.START_BRACKET);
                        break;
                    case ']':
                        readyToken = makeToken(TokenType.END_BRACKET);
                        break;
                    case ':':
                        readyToken = makeToken(TokenType.COLON);
                        break;
                    case ',':
                        readyToken = makeToken(TokenType.COMMA);
                        break;
                    case '"':
                        readyToken = makeToken(TokenType.STRING, finishQuote());
                        break;
                    case ' ':
                    case '\r':
                    case '\t':
                        // skippable whitespace
                        break;
                    case '\n':
                        charLoc = 0;
                        lineLoc++;
                        break;
                    default:
                        // unknown characters?
                        // numbers like 52.03
                        buff.append(c);
                        break;
                }
                if(readyToken != null){
                    if(buff.length() > 0){
                        return new Token(TokenType.PROPERTY, buff.toString());
                    }else{
                        Token t = readyToken;
                        readyToken = null;
                        return t;
                    }
                }
            }

        } catch (IOException ex) {
            throw new DecodeException(ex);
        }
    }
    private boolean isCharacterGood(char c){
        if(c >= '0' && c <= '9'){
            return true;
        }
        if(c >= 'a' && c <= 'z'){
            return true;
        }
        if(c >= 'A' && c <= 'Z'){
            return true;
        }
        switch(c){
            case '.':
                return true;
            case '-':
                return true;
            default:
                return false;
        }
    }

    private String finishQuote(){
        StringBuilder buff = new StringBuilder();
        char last = '\0';

        try {
            while(true){
                int read = in.read();
                charLoc++;
                if(read == -1){
                    throw new Error("End of file reached!");
                }

                char c = (char) read;

                if(c == '\n'){
                    charLoc = 0;
                    lineLoc++;
                }

                if(c == '"' && last != '\\'){
                    return JSONUtils.restoreString(buff.toString());
                }

                last = c;

                buff.append(c);
            }

        } catch (IOException ex) {
            throw new Error(ex);
        }
    }
    
    private Token makeToken(TokenType type){
        return new Token(type, type.name);
    }
    private Token makeToken(TokenType type, String buff){
        return new Token(type, buff);
    }
    
    public static class Token{
        public final TokenType type;
        public final String buf;

        public Token(TokenType type, String buf) {
            this.type = type;
            this.buf = buf;
        }

        @Override
        public String toString() {
            return String.format("Token[%s, %s]", type.getName(), buf);
        }
    }


    public enum TokenType{
        START_BRACE("{"),
        END_BRACE("}"),
        START_BRACKET("["),
        END_BRACKET("]"),
        COLON(":"),
        COMMA(","),
        PROPERTY("property"),
        STRING("string"),
        EOF("EOF");

        private final String name;
        private TokenType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Token " + name;
        }
    }
}
