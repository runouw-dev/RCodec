/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 * An interface that marks an Object as being encodable from a CoderArray
 * @author Robert
 */
public interface CodableArrayEncode {
    CoderArray encode(CoderArray encoder);
}
