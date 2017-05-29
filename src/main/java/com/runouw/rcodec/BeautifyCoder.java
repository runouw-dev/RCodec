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
