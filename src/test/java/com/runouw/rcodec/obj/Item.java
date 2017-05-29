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

/**
 *
 * @author Robert
 */
public class Item implements Codable{
    public static boolean USE_GENERIC_ARRAY = false;
    
    private String id = "Item";
    private String[] tags = new String[]{"group1", "group2"};
    private double x = 0;
    private double y = 0;
    
    private double scaleX = 1;
    private double scaleY = 1;
    private double rotation = 0;

    private ColorTransform colors = new ColorTransform();
    
    @Override
    public CoderNode encode(CoderNode encoder) {
        if(!USE_GENERIC_ARRAY){
            return encoder
                    .set("id", id)
                    .set("x", x)
                    .set("y", y)
                    .set("scaleX", scaleX)
                    .set("scaleY", scaleY)
                    .set("rotation", rotation)
                    .set("colors", colors)
                    .withArray("tags", arr -> {
                        for (String tag : tags) {
                            arr.add(tag);
                        }
                    });
        }else{
            return encoder
                    .set("id", id)
                    .set("pos", new double[]{x, y})
                    .set("scale", new double[]{scaleX, scaleY})
                    .set("rotation", rotation)
                    .set("colors", colors)
                    .withArray("tags", arr -> {
                        for (String tag : tags) {
                            arr.add(tag);
                        }
                    });
        }
    }

    @Override
    public void decode(CoderNode decoder) {
        decoder
                .ifString("id", val -> id = val)
                .ifDouble("x", val -> x = val)
                .ifDouble("y", val -> y = val)
                .ifDouble("scaleX", val -> scaleX = val)
                .ifDouble("scaleY", val -> scaleY = val)
                .ifDouble("rotation", val -> rotation = val)
                .ifNode("colors", ct -> colors.decode(ct))
                .ifArray("tags", arr -> {
                    tags = new String[arr.size()];
                    for(int i=0;i<arr.size();i++){
                        tags[i] = arr.getString(i, null);
                    }
                });
    }
    
    public static Item generate(){
        Item item = new Item();
        item.x = Math.random() * 9999;
        item.y = Math.random() * 9999;
        item.scaleX = Math.random() * 2 - 1;
        item.scaleY = Math.random() * 2 - 1;
        item.rotation = Math.random() * 360 - 180;
        
        double br = Math.random() * .8 + .2;
        item.colors.multipliers.r = br;
        item.colors.multipliers.g = br;
        item.colors.multipliers.b = br;
        
        item.colors.offset.r = 1 - br;
        item.colors.offset.b = 1 - br;
        item.colors.offset.g = 1 - br;
        
        item.colors.hsl.h = Math.random() * 360;
        
        return item;
    }
    
}
