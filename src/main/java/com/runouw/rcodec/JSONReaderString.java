/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class JSONReaderString implements JSONReader {
    private final JSONTokenizer tokenizer;
    
    public JSONReaderString(String string) {
        tokenizer = new JSONTokenizer(new StringReader(string));
    }
    public JSONReaderString(byte[] bytes) {
        String str = new String(bytes, Charset.forName("UTF8"));
        tokenizer = new JSONTokenizer(new StringReader(str));
    }

    @Override
    public JSONTokenizer.Token getNextToken() {
        return tokenizer.nextToken();
    }
}
