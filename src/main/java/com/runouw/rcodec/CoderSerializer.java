/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import com.runouw.rcodec.EncodeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Provides encoding routines for data types that aren't supported
 * in CoderData.Type.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public class CoderSerializer {
    private final Map<Class<?>, ClassEncoder> encoderMap = new HashMap<>();
    private final List<ClassEncoder> encoderList = new ArrayList<>();
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public CoderSerializer() {
        addEncoder(Optional.class, (s, o) -> o.orElse(null));
        
        addGenericArrayEncoders();
        
        // Object array
        addEncoder(makeSerializer(cl -> {
            return cl.isArray();
        }, (s, val) -> {
            Object[] arr = (Object[]) val;
            CoderArray r = new CoderArray(s);
            for(Object v:arr){
                r.add(v);
            }
            return r;
        }));
        
        // Collection
        addEncoder(cl -> 
                Collection.class.isAssignableFrom(cl)
        , (s, arr) -> {
            Collection c = (Collection) arr;
            CoderArray r = new CoderArray(s);
            for(Object val:c){
                r.add(val);
            }
            return r;
        });
        
        // Map
        addEncoder(cl ->
                Map.class.isAssignableFrom(cl)
                , (s, map) -> {
                    Map m = (Map) map;
                    
            CoderNode r = new CoderNode(s);
            for(Object key:m.keySet()){
                r.set(key.toString(), m.get(key));
            }
            return r;
        });
    }
    
    /**
     * Adds an encoder to this CoderSerializer.
     * @param encoder classEncoder.
     */
    public final void addEncoder(ClassEncoder encoder){
        this.encoderList.add(encoder);
    }
    
    private void addGenericArrayEncoders(){
        addEncoder(byte[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(byte val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(boolean[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(boolean val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(short[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(short val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(char[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(char val:arr){
                r.add((short) val);
            }
            return r;
        });
        addEncoder(int[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(int val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(long[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(long val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(float[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(float val:arr){
                r.add(val);
            }
            return r;
        });
        addEncoder(double[].class, (s, arr) -> {
            CoderArray r = new CoderArray(s);
            for(double val:arr){
                r.add(val);
            }
            return r;
        });
    }
    
    public Object encode(CoderSettings settings, Object val){
        if(val == null){
            return null;
        }
        
        Class<?> cl = val.getClass();
        
        ClassEncoder cs = encoderMap.get(cl);
        if(cs == null){
            cs = encoderList.stream()
                    .filter(s -> s.supportsClass(cl))
                    .findFirst().orElse(null);
            if(cs == null){
                return encodeFail(val);
            }
        }
        
        return cs.encode(settings, val);
    }
    
    private Object encodeFail(Object val){
        throw new EncodeException("Class " + val.getClass().getName() + " cannot be encoded!");
    }
    
    /**
     * Provides a class matching test and a supplier for the Encodable object.
     */
    public interface ClassEncoder {
        /**
         * Checks if this ClassEncoder can encode this class. This is usually
         * an equals or isAssignableFrom test.
         * @param cl the class type to test
         * @return 
         */
        boolean supportsClass(Class<?> cl);
        
        /**
         * Transforms the supplied object into a type that can be more readily
         * encoded.
         * 
         * The returned value isn't required to be an encodable type. For
         * instance, if an Optional<List<Integer>> was passed in, this transform
         * method is only expected to unwrap the type by one layer, therefore
         * returning List<Integer> which will be encoded by another\
         * ClassEncoder.
         * @param settings CoderSettings so that certain settings can be queried
         * or so that a new CoderNode or CoderArray can be constructed.
         * @param val
         * @return 
         */
        Object encode(CoderSettings settings, Object val);
    }
    
    /**
     * Adds an encoder for the specified class only if the class type matches
     * exactly.
     * @param <T>
     * @param cl class type to match with the equals method.
     * @param transform the transform method as described in
     * ClassEncoder.encode.
     */
    public final <T> void addEncoder(Class<T> cl, BiFunction<CoderSettings, T, Object> transform){
        encoderMap.put(cl, makeSerializer(b -> cl.equals(b), transform));
    }
    
    /**
     * Adds an encoder for the specified class if the class matches the supplied
     * predicate.
     * @param <T>
     * @param matches a predicate to match classes with
     * @param transform the transform method as described in
     * ClassEncoder.encode.
     */
    public final <T> void addEncoder(Predicate<Class<?>> matches, BiFunction<CoderSettings, T, Object> transform){
        encoderList.add(makeSerializer(matches, transform));
    }
    
    /**
     * Convenience method to construct a ClassEncoder in one line.
     * predicate.
     * @param <T>
     * @param matches a predicate to match classes with
     * @param transform the transform method as described in
     * ClassEncoder.encode.
     * @return 
     */
    public final <T> ClassEncoder makeSerializer(Predicate<Class<?>> matches, BiFunction<CoderSettings,T, Object> transform){
        return new ClassEncoder() {
            @Override
            public boolean supportsClass(Class<?> cl) {
                return matches.test(cl);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Object encode(CoderSettings settiungs, Object val) {
                return transform.apply(settiungs, (T) val);
            }
        };
    }
    
    /**
     * Returns the default implementation of CoderSerializer. 
     * @return 
     */
    public static CoderSerializer getDefault(){
        return Holder.INSTANCE;
    }
    
    private static class Holder{
        private static final CoderSerializer INSTANCE = new CoderSerializer();
    }
}
