/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * An immutable wrapper for a CoderContainer that provides some JSON formatting
 * rules.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation 
 */
public final class BeautifyCoder {
    private final CoderContainer<?, ?> container;
    private final BeautifyRules beautifyRules;

    BeautifyCoder(
            final CoderContainer<?, ?> container, 
            final BeautifyRules beautifyRules) {
        
        this.container = container;
        this.beautifyRules = beautifyRules;
    }

    BeautifyCoder withCoderNode(final CoderNode container){
        return new BeautifyCoder(container, beautifyRules);
    }

    BeautifyCoder withIndentation(final Indentation indentation){
        return new BeautifyCoder(container, beautifyRules.withIndentation(indentation));
    }

    BeautifyCoder withSpacing(final int spacing){
        return new BeautifyCoder(container, beautifyRules.withSpacing(spacing));
    }

    @Override
    public String toString() {
        return container.write(new JSONWriterStringBuilder(beautifyRules)).toString();
    }

    void writeToFile(final Path path) throws IOException {
        container.writeJSONFile(path, beautifyRules);
    }

    void write(final OutputStream out) throws IOException {
        container.writeJSON(out, beautifyRules);
    }
}
