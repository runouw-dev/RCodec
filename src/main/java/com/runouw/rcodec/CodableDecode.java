/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 * An interface that marks an Object as being decodeable from a CoderNode
 * @author Robert
 */
public interface CodableDecode {
    void decode(CoderNode decoder);
}
