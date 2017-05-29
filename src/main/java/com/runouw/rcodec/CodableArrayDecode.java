/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 * An interface that marks an Object as being decodable from a CoderArray
 * @author Robert
 */
public interface CodableArrayDecode {
    void decode(CoderArray decoder);
}
