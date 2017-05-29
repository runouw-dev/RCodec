/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec.obj;

import com.runouw.rcodec.Codable;
import com.runouw.rcodec.CoderNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
public class ItemList implements Codable{
    public List<Item> items = new ArrayList<>();

    public ItemList() {
    }
    
    @Override
    public CoderNode encode(CoderNode encoder) {
        encoder.withArray("items", arr -> {
            for(Item item:items){
                arr.add(item);
            }
        });
        
        return encoder;
    }

    @Override
    public void decode(CoderNode decoder) {
        items.clear();
        
        decoder.withArray("items", arr -> {
            for(int i=0;i<arr.size();i++){
                arr.getNode(i).ifPresent(node -> {
                    Item newItem = new Item();
                    newItem.decode(node);
                    items.add(newItem);
                });
            }
        });
    }
    
}
