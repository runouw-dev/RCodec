/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Robert
 */
public class TestBJSON {
    private static final double EPSILON = 1E-7;
    
    @Test
    public void testBJSONNull(){
        CoderNode node = new CoderNode();
        node.set("isNull", null);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(null, node.getBoolean("isNull").orElse(null));
        assertEquals(null, node.getBoolean("isEmpty").orElse(null));
    }
    
    @Test
    public void testBJSONBool(){
        Stream.of(true, false).forEach(value -> {
            CoderNode node = new CoderNode();
            node.set("bool", value);

            // re-encode
            node = new CoderNode().fromBytes(node.toBytes());

            assertEquals(value, node.getBoolean("bool").get());
        });
    }
    
    @Test
    public void testBJSONByte(){
        IntStream.of(0, 255).forEach(i -> {
            byte value = (byte) i;

            CoderNode node = new CoderNode();
            node.set("byte", value);

            // re-encode
            node = new CoderNode().fromBytes(node.toBytes());

            assertEquals(value, node.getByte("byte").get().byteValue());
        });
    }
    
    @Test
    public void testBJSONShort(){
        short value = (short) (Math.random() * Short.MAX_VALUE);
        
        CoderNode node = new CoderNode();
        node.set("short", value);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(value, node.getShort("short").get().shortValue());
    }
    
    @Test
    public void testBJSONInteger(){
        int value = (int) (Math.random() * Integer.MAX_VALUE);
        
        CoderNode node = new CoderNode();
        node.set("int", value);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(value, node.getInt("int").get().intValue());
    }
    
    @Test
    public void testBJSONLong(){
        long value = (long) (Math.random() * Long.MAX_VALUE);
        
        CoderNode node = new CoderNode();
        node.set("long", value);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(value, node.getLong("long").get().longValue());
    }
    
    @Test
    public void testBJSONFloat(){
        float value = (float) (Math.random() * Float.MAX_VALUE);
        
        CoderNode node = new CoderNode();
        node.set("float", value);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(value, node.getFloat("float").get(), 0f);
    }
    
    @Test
    public void testBJSONDouble(){
        double value = (double) (Math.random() * Double.MAX_VALUE);
        
        CoderNode node = new CoderNode();
        node.set("double", value);
        
        // re-encode
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(value, node.getDouble("double").get(), 0d);
    }
    
    @Test
    public void testBJSONTypesInArray(){
        CoderArray arr = new CoderArray();
        
        boolean boolValue = true;
        byte byteValue = (byte) 223;
        short shortValue = (short) 66573;
        int intValue = 3891;
        long longValue = 0x1122334455667788L;
        float floatValue = 10.014422f;
        double doubleValue = Math.PI;
        String stringValue = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()1234567890;''\\\"\"";
        byte[] byteArrayValue = new byte[]{
            (byte) 'b', (byte) 'a', (byte) 's', (byte) 'e', (byte) '6', (byte) '4',
            (byte) ' ', (byte) 'a', (byte) 's', (byte) 'c', (byte) 'i', (byte) 'i'
        };
        
        
        arr.add(boolValue);
        arr.add(byteValue);
        arr.add(shortValue);
        arr.add(intValue);
        arr.add(longValue);
        arr.add(floatValue);
        arr.add(doubleValue);
        arr.add(stringValue);
        arr.add(byteArrayValue);
        
        arr = new CoderArray().fromBytes(arr.toBytes());
        
        assertEquals(boolValue, arr.getBoolean(0).get());
        assertEquals(byteValue, arr.getByte(1).get().byteValue());
        assertEquals(shortValue, arr.getShort(2).get().shortValue());
        assertEquals(intValue, arr.getInt(3).get().intValue());
        assertEquals(longValue, arr.getLong(4).get().longValue());
        assertEquals(floatValue, arr.getFloat(5).get(), 0.0f);
        assertEquals(doubleValue, arr.getDouble(6).get(), 0.0);
        assertEquals(stringValue, arr.getString(7).get());
        
        byte[] getByteArray = arr.getByteArray(8).get();
        
        assertEquals(getByteArray.length, byteArrayValue.length);
        for(int i=0;i<byteArrayValue.length;i++){
            assertEquals(getByteArray[i], byteArrayValue[i]);
        }
    }
    
    @Test
    public void testBJSONTypesInNode(){
        CoderNode node = new CoderNode();
        
        boolean boolValue = true;
        byte byteValue = (byte) 223;
        short shortValue = (short) 66573;
        int intValue = 3891;
        long longValue = 0x1122334455667788L;
        float floatValue = 10.014422f;
        double doubleValue = Math.PI;
        String stringValue = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()1234567890;''\\\"\"";
        byte[] byteArrayValue = new byte[]{
            (byte) 'b', (byte) 'j', (byte) 's', (byte) 'o', (byte) 'n',
            (byte) ' ', (byte) 'i', (byte) 'n',
            (byte) ' ', (byte) 'a', (byte) 's', (byte) 'c', (byte) 'i', (byte) 'i'
        };
        
        
        node.set("boolValue", boolValue);
        node.set("byteValue", byteValue);
        node.set("shortValue", shortValue);
        node.set("intValue", intValue);
        node.set("longValue", longValue);
        node.set("floatValue", floatValue);
        node.set("doubleValue", doubleValue);
        node.set("stringValue", stringValue);
        node.set("byteArrayValue", byteArrayValue);
        
        node = new CoderNode().fromBytes(node.toBytes());
        
        assertEquals(boolValue, node.getBoolean("boolValue").get());
        assertEquals(byteValue, node.getByte("byteValue").get().byteValue());
        assertEquals(shortValue, node.getShort("shortValue").get().shortValue());
        assertEquals(intValue, node.getInt("intValue").get().intValue());
        assertEquals(longValue, node.getLong("longValue").get().longValue());
        assertEquals(floatValue, node.getFloat("floatValue").get(), 0.0f);
        assertEquals(doubleValue, node.getDouble("doubleValue").get(), 0.0);
        assertEquals(stringValue, node.getString("stringValue").get());
        
        byte[] getByteArray = node.getByteArray("byteArrayValue").get();
        
        assertEquals(getByteArray.length, byteArrayValue.length);
        for(int i=0;i<byteArrayValue.length;i++){
            assertEquals(getByteArray[i], byteArrayValue[i]);
        }
    }
    
    @Test
    public void testBJSONNested(){
        CoderNode root = new CoderNode()
            .withNode("element", element -> {
                element.set("frame", 1.0d)
                .withNode("common", common -> {
                    common.withNode("position", position -> position
                        .set("x", 1.0)
                        .set("y", 2.0)
                    );
                    common.withNode("scale", scale -> scale
                        .set("x", 100.0)
                        .set("y", 100.0)
                    );
                })
                .set("speed", "too \\ \" fast")
                .withArray("arr", arr -> arr
                    .add(1.0)
                    .add(2.0)
                    .addArray(arr2 -> arr2
                        .add(1)
                        .add(2)
                    )
                    .addNode(node -> node
                        .set("x", 1.0)
                        .set("y", 1.0)
                        .set("z", 1.0)
                    )
                );
            });
        
        CoderNode decoded = new CoderNode().fromBytes(root.toBytes());
        decoded
            .ifNode("element", element -> {
                assertEquals(1.0, element.getDouble("frame").get(), EPSILON);
                
                element.withNode("common", common -> {
                    common.withNode("position", position -> {
                        assertEquals(1.0, position.getDouble("x").get(), EPSILON);
                        assertEquals(2.0, position.getDouble("y").get(), EPSILON);
                    });
                    common.withNode("scale", scale -> {
                        assertEquals(100.0, scale.getDouble("x").get(), EPSILON);
                        assertEquals(100.0, scale.getDouble("y").get(), EPSILON);
                    });
                });
                
                assertEquals("too \\ \" fast", element.getString("speed", "derp"));
                
                element.withArray("arr", arr -> {
                    assertEquals(1.0, arr.getDouble(0).get(), EPSILON);
                    assertEquals(2.0, arr.getDouble(1).get(), EPSILON);
                    
                    arr.withArray(2, arr2 -> {
                        assertEquals(1, arr2.getInt(0).get().intValue());
                        assertEquals(2, arr2.getInt(1).get().intValue());
                    });
                    
                    arr.withNode(3, node -> {
                        assertEquals(1.0, node.getDouble("x").get(), EPSILON);
                        assertEquals(1.0, node.getDouble("y").get(), EPSILON);
                        assertEquals(1.0, node.getDouble("z").get(), EPSILON);
                    });
                });
            });
    }
}
