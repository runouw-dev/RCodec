/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec.obj;

import com.runouw.rcodec.Codable;
import com.runouw.rcodec.CoderNode;

/**
 *
 * @author runou
 */
public class ColorTransform implements Codable {
    public enum Model{
        NORMAL, LEGACY;
    } public Model model = Model.NORMAL;

    public static class Multipliers{
        public double r, g, b, a;

        public Multipliers() {
            r = g = b = a = 1;
        }
    } public Multipliers multipliers = new Multipliers();
    public static class Offset{
        public double r, g, b, a;

        public Offset() {
        }
    } public Offset offset = new Offset();
    public static class HSL{
        public double h, s, l;

        public HSL() {

        }
    } public HSL hsl = new HSL();
    public static class Misc{
        public double br, c;

        public Misc() {
        }
    } public Misc misc = new Misc();

    @Override
    public CoderNode encode(CoderNode encoder){
        if(!Item.USE_GENERIC_ARRAY){
            encoder.set("Model", model.toString());
            encoder.withArray("multipliers", arr -> arr
                    .add(multipliers.a)
                    .add(multipliers.r)
                    .add(multipliers.g)
                    .add(multipliers.b)
            );
            encoder.withArray("offset", arr -> arr
                    .add(offset.a)
                    .add(offset.r)
                    .add(offset.g)
                    .add(offset.b)
            );
            encoder.withArray("hsl", arr -> arr
                    .add(hsl.h)
                    .add(hsl.s)
                    .add(hsl.l)
            );
            encoder.withNode("misc", node -> node
                    .set("br", misc.br)
                    .set("c", misc.c)
            );
        }else{
            encoder.set("Model", model.toString());
            encoder.set("multipliers", new double[]{
                multipliers.r,
                multipliers.g,
                multipliers.b,
                multipliers.a
            });
            encoder.set("offset", new double[]{
                offset.r,
                offset.g,
                offset.b,
                offset.a
            });
            encoder.set("hsl", new double[]{
                hsl.h,
                hsl.s,
                hsl.l
            });
            encoder.set("misc", new double[]{
                misc.br,
                misc.c
            });
        }
        return encoder;
    }

    @Override
    public void decode(CoderNode decoder) {
        decoder.withArray("multipliers", arr -> arr
                .ifDouble(0, v -> multipliers.r = v)
                .ifDouble(1, v -> multipliers.g = v)
                .ifDouble(2, v -> multipliers.b = v)
                .ifDouble(3, v -> multipliers.a = v)
        ).withArray("offset", arr -> arr
                .ifDouble(0, v -> offset.r = v)
                .ifDouble(1, v -> offset.g = v)
                .ifDouble(2, v -> offset.b = v)
                .ifDouble(3, v -> offset.a = v)
        ).withArray("hsl", arr -> arr
                .ifDouble(0, v -> hsl.h = v)
                .ifDouble(1, v -> hsl.s = v)
                .ifDouble(2, v -> hsl.l = v)
        ).withNode("misc", node -> node
                .ifDouble("br", v -> misc.br = v)
                .ifDouble("c", v -> misc.c = v)
        );
    }
}
