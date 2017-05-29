/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 * An interface that marks an Object as being serializable to JSON/BJSON
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public interface Codable extends CodableEncode, CodableDecode{
}
