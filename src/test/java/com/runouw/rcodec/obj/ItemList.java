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
