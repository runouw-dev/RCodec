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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Robert
 */
public class CoerceTest {
    @Test
    public void coerceStringTo(){
        CoderNode arr = new CoderNode();
        arr.set("num", "25");
        arr.set("trueBool", "true");
        arr.set("falseBool", "false");
        
        assertEquals(arr.getBoolean("trueBool", false), true);
        assertEquals(arr.getBoolean("falseBool", true), false);
        
        assertEquals(arr.getByte("num", (byte) 0), (byte) 25);
        assertEquals(arr.getShort("num", (short) 0), (byte) 25);
        assertEquals(arr.getInt("num", 0), 25);
        assertEquals(arr.getLong("num", 0), 25);
        assertEquals(arr.getFloat("num", 0), 25, 0.000);
        assertEquals(arr.getDouble("num", 0), 25, 0.000);
        
        assertEquals(arr.getBoolean("trueBool").orElse(null), true);
        assertEquals(arr.getBoolean("falseBool").orElse(null), false);
        
        assertEquals(arr.getShort("num").orElse(null), Short.valueOf((byte) 25));
        assertEquals(arr.getShort("num").orElse(null), Short.valueOf((byte) 25));
        assertEquals(arr.getInt("num").orElse(null), Integer.valueOf((byte) 25));
        assertEquals(arr.getFloat("num").orElse(null), Float.valueOf((byte) 25));
        assertEquals(arr.getDouble("num").orElse(null), Double.valueOf((byte) 25));
    }
    
    @Test
    public void coerceNumberTo(){
        CoderNode arr = new CoderNode();
        arr.set("num", 25);
        arr.set("trueBool", true);
        arr.set("falseBool", false);
        
        assertEquals(arr.getBoolean("trueBool", false), true);
        assertEquals(arr.getBoolean("falseBool", true), false);
        
        assertEquals(arr.getInt("trueBool", 0), 1);
        assertEquals(arr.getInt("falseBool", 1), 0);
        
        assertEquals(arr.getByte("num", (byte) 0), (byte) 25);
        assertEquals(arr.getShort("num", (short) 0), (byte) 25);
        assertEquals(arr.getInt("num", 0), 25);
        assertEquals(arr.getLong("num", 0), 25);
        assertEquals(arr.getFloat("num", 0), 25, 0.000);
        assertEquals(arr.getDouble("num", 0), 25, 0.000);
        
        assertEquals(arr.getBoolean("trueBool").orElse(null), true);
        assertEquals(arr.getBoolean("falseBool").orElse(null), false);
        
        assertEquals(arr.getInt("trueBool").orElse(null).intValue(), 1);
        assertEquals(arr.getInt("falseBool").orElse(null).intValue(), 0);
        
        assertEquals(arr.getShort("num").orElse(null), Short.valueOf((byte) 25));
        assertEquals(arr.getShort("num").orElse(null), Short.valueOf((byte) 25));
        assertEquals(arr.getInt("num").orElse(null), Integer.valueOf((byte) 25));
        assertEquals(arr.getFloat("num").orElse(null), Float.valueOf((byte) 25));
        assertEquals(arr.getDouble("num").orElse(null), Double.valueOf((byte) 25));
    }
}
