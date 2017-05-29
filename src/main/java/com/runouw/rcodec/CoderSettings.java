/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 * A class that contains serialization rules for CoderNode and CoderArray. This
 * class can be extended to provide even more specific encoding rules.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public class CoderSettings {
    public CoderSettings() {
    }

    public CoderSerializer getSerializer() {
        return CoderSerializer.getDefault();
    }
}
