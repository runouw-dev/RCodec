/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

/**
 *
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
public final class BeautifyRules {

    private final Indentation indentation;
    private final int spacing;
    private final int arrayWrappingLength = 80;

    /**
     * BeautifyRules for encoding JSON packed into a single line no whitespace.
     */
    public static final BeautifyRules MINIFIED = new BeautifyRules(Indentation.MINIFIED, 0);
    /**
     * BeautifyRules for encoding JSON with scope indentation of 2 spaces.
     */
    public static final BeautifyRules SPACES_X_2 = new BeautifyRules(Indentation.SPACES, 2);
    /**
     * BeautifyRules for encoding JSON with scope indentation of 4 spaces.
     */
    public static final BeautifyRules SPACES_X_4 = new BeautifyRules(Indentation.SPACES, 4);
    /**
     * BeautifyRules for encoding JSON with scope indentation of 1 tab.
     */
    public static final BeautifyRules TABS = new BeautifyRules(Indentation.TABS, 1);

    /**
     * Constructs a new set of JSON formatting rules using the defaults of 2
     * spaces for scope indentation.
     */
    public BeautifyRules() {
        this(Indentation.SPACES, 2);
    }

    /**
     * Constructs a new set of JSON formatting rules.
     *
     * @param indentation the type of indentation to use
     * @param spacing the amount of whitespace.
     */
    public BeautifyRules(
            final Indentation indentation,
            final int spacing) {

        this.indentation = indentation;
        this.spacing = spacing;
    }

    /**
     * Constructs a new instance of BeautifyRules based on the current instance
     * using the specified Indentation type.
     *
     * @param indentation the type of Indentation
     * @return the new instance of BeautifyRules.
     */
    public BeautifyRules withIndentation(final Indentation indentation) {
        return new BeautifyRules(indentation, spacing);
    }

    /**
     * Constructs a new instance of BeautifyRules based on the current instance
     * using the specified amount of whitespace.
     *
     * @param spacing the amount of whitespace.
     * @return the new instance of BeautifyRules.
     */
    public BeautifyRules withSpacing(int spacing) {
        return new BeautifyRules(indentation, spacing);
    }

    /**
     * Retrieves the type of Indentation.
     *
     * @return the Indentation type.
     */
    public Indentation getIndentation() {
        return indentation;
    }

    /**
     * Retrieves the size required before array wrapping occurs.
     *
     * @return the number of characters before array wrapping.
     */
    public int getArrayWrappingLength() {
        return arrayWrappingLength;
    }

    /**
     * Retrieves the amount of whitespace used to indicate new scope levels.
     *
     * @return the amount of whitespace.
     */
    public int getSpacing() {
        return spacing;
    }
}
