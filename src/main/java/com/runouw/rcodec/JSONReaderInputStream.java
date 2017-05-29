/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
final class JSONReaderInputStream implements JSONReader, AutoCloseable{
    private final InputStreamReader isr;
    private final JSONTokenizer tokenizer;
    
    public JSONReaderInputStream(InputStream in) {
        this.isr = new InputStreamReader(in);
        this.tokenizer = new JSONTokenizer(isr);
    }

    @Override
    public JSONTokenizer.Token getNextToken() {
        return tokenizer.nextToken();
    }

    @Override
    public void close() throws IOException {
        isr.close();
    }
}
