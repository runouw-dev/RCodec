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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.LoggerFactory;

/**
 * Interfaced shared by CoderNode and CoderArray that provides methods to encode
 * and decode various generic types of data.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
interface CoderContainer<Key, Self> {

    /**
     * Gets the coder data that belongs to the key
     *
     * @param key the key
     * @return the value.
     */
    CoderData getCoderData(Key key);

    Self setCoderData(Key key, CoderData data);

    CoderSettings getSettings();

    //NOTE: Object type allows you to place "null" types with null checking
    //NOTE: As well as allows support for CoderSerializer to try encoding it
    default Self set(Key key, Object value) {
        if (value == null) {
            return setCoderData(key, new CoderData(CoderData.Type.NULL, null));
        } else {
            if (value instanceof CodableEncode) {
                value = ((CodableEncode) value).encode(new CoderNode(getSettings()));
            } else if (value instanceof CodableArrayEncode) {
                value = ((CodableArrayEncode) value).encode(new CoderArray(getSettings()));
            }

            final Optional<CoderData.Type> type = CoderData.Type.fromClass(value.getClass());

            if (type.isPresent()) {
                return setCoderData(key, new CoderData(type.get(), value));
            } else {
                return set(key, getSettings().getSerializer().encode(getSettings(), value));
            }
        }
    }

    default <T> Optional<T> get(Key key, Class<T> cl) {
        final CoderData d = getCoderData(key);

        if (d == null || d.get() == null || d.getType() == CoderData.Type.NULL) {
            return Optional.empty();
        }

        try {
            final T casted = d.getCast(cl);

            return Optional.of(casted);
        } catch (ClassCastException ex) {
            LoggerFactory.getLogger(this.getClass())
                    .error("Unable to cast value to type [{}]!", cl.getName());

            return Optional.empty();
        }
    }

    default Optional<Boolean> getBoolean(Key key) {
        return get(key, Boolean.class);
    }

    default Optional<Byte> getByte(Key key) {
        return get(key, Byte.class);
    }

    default Optional<Short> getShort(Key key) {
        return get(key, Short.class);
    }

    default Optional<Integer> getInt(Key key) {
        return get(key, Integer.class);
    }

    default Optional<Long> getLong(Key key) {
        return get(key, Long.class);
    }

    default Optional<Float> getFloat(Key key) {
        return get(key, Float.class);
    }

    default Optional<Double> getDouble(Key key) {
        return get(key, Double.class);
    }

    default Optional<String> getString(Key key) {
        return get(key, String.class);
    }

    default Optional<byte[]> getByteArray(Key key) {
        return get(key, byte[].class);
    }

    default Optional<CoderNode> getNode(Key key) {
        return get(key, CoderNode.class);
    }

    default Optional<CoderArray> getArray(Key key) {
        return get(key, CoderArray.class);
    }

    default boolean getBoolean(Key key, boolean def) {
        return get(key, Boolean.class).orElse(def);
    }

    default byte getByte(Key key, byte def) {
        return get(key, Byte.class).orElse(def);
    }

    default short getShort(Key key, short def) {
        return get(key, Short.class).orElse(def);
    }

    default int getInt(Key key, int def) {
        return get(key, Integer.class).orElse(def);
    }

    default long getLong(Key key, long def) {
        return get(key, Long.class).orElse(def);
    }

    default float getFloat(Key key, float def) {
        return get(key, Float.class).orElse(def);
    }

    default double getDouble(Key key, double def) {
        return get(key, Double.class).orElse(def);
    }

    default String getString(Key key, String def) {
        return get(key, String.class).orElse(def);
    }

    default byte[] getByteArray(Key key, byte[] def) {
        return get(key, byte[].class).orElse(def);
    }

    default CoderNode getNode(Key key, CoderNode def) {
        return get(key, CoderNode.class).orElse(def);
    }

    default CoderArray getArray(Key key, CoderArray def) {
        return get(key, CoderArray.class).orElse(def);
    }

    default Self ifBoolean(Key key, Consumer<Boolean> consumer) {
        getBoolean(key).ifPresent(consumer);
        return self();
    }

    default Self ifByte(Key key, Consumer<Byte> consumer) {
        getByte(key).ifPresent(consumer);
        return self();
    }

    default Self ifShort(Key key, Consumer<Short> consumer) {
        getShort(key).ifPresent(consumer);
        return self();
    }

    default Self ifInt(Key key, Consumer<Integer> consumer) {
        getInt(key).ifPresent(consumer);
        return self();
    }

    default Self ifFloat(Key key, Consumer<Float> consumer) {
        getFloat(key).ifPresent(consumer);
        return self();
    }

    default Self ifDouble(Key key, Consumer<Double> consumer) {
        getDouble(key).ifPresent(consumer);
        return self();
    }

    default Self ifString(Key key, Consumer<String> consumer) {
        getString(key).ifPresent(consumer);
        return self();
    }

    default Self ifByteArray(Key key, Consumer<byte[]> consumer) {
        getByteArray(key).ifPresent(consumer);
        return self();
    }

    default Self ifNode(Key key, Consumer<CoderNode> onNode) {
        getNode(key).ifPresent(onNode);

        return self();
    }

    default Self ifArray(Key key, Consumer<CoderArray> onNode) {
        getArray(key).ifPresent(onNode);

        return self();
    }

    default <T> Self ifArrayForEach(Key key, Class<T> cl, Consumer<T> onData) {
        getArray(key).ifPresent(arr -> arr.forEach(cl, onData));

        return self();
    }

    @SuppressWarnings("unchecked")
    default <T> T getOrElse(Key key, T def) {
        return ((Optional<T>) get(key, (def == null) ? null : def.getClass())).orElse(def);
    }

    default Self withNode(Key key, Consumer<CoderNode> onNode) {
        final CoderNode node = getNode(key).orElseGet(CoderNode::new);

        set(key, node);
        onNode.accept(node);

        return self();
    }

    default Self withArray(Key key, Consumer<CoderArray> onNode) {
        final CoderArray array = getArray(key).orElseGet(CoderArray::new);

        set(key, array);
        onNode.accept(array);

        return self();
    }

    Self self();

    default BeautifyCoder withBeautify() {
        return new BeautifyCoder(this, BeautifyRules.SPACES_X_4);
    }

    default BeautifyCoder withBeautify(BeautifyRules rules) {
        return new BeautifyCoder(this, rules);
    }

    JSONWriter write(JSONWriter writer);

    ByteWriter write(ByteWriter writer);

    default Self read(ByteReader reader) {
        return read(reader, true);
    }

    default Self read(JSONReader reader) {
        return read(reader, true);
    }

    Self read(ByteReader reader, boolean readHeader);

    Self read(JSONReader reader, boolean readHeader);

    default byte[] toBytes() {
        final ByteWriterByteArray b = new ByteWriterByteArray();
        // b.writeHeader(); // now we only writer the header if it's a file
        write(b);
        b.write('\0');

        return b.toBytes();
    }

    default String asString() {
        return write(new JSONWriterStringBuilder()).toString();
    }

    default String toString(BeautifyRules rules) {
        return write(new JSONWriterStringBuilder(rules)).toString();
    }

    default String toMinifiedString() {
        return write(new JSONWriterStringBuilder(BeautifyRules.MINIFIED)).toString();
    }

    default void writeBJSON(OutputStream out) {
        write(
                new ByteWriterOutputStream<>(out)
                        .writeHeader()
        ).write((byte) '\0');
    }

    default void writeJSON(OutputStream out) {
        write(new JSONWriterStream(out, new BeautifyRules(Indentation.MINIFIED, 0)));
    }

    default void writeJSON(OutputStream out, BeautifyRules rules) {
        write(new JSONWriterStream(out, rules));
    }

    default void writeBJSONFile(Path path) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            writeBJSON(out);
        }
    }

    default void writeJSONFile(Path path) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            writeJSON(out);
        }
    }

    default void writeJSONFile(Path path, BeautifyRules rules) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            writeJSON(out, rules);
        }
    }

    default Self fromBuffer(final ByteBuffer buffer) {
        read(new ByteReaderByteArray(buffer));

        return self();
    }

    default Self fromBytes(byte[] bytes) {
        read(new ByteReaderByteArray(bytes));

        return self();
    }

    default Self fromBytes(byte[] bytes, final int off, final int len) {
        read(new ByteReaderByteArray(bytes, off, len));

        return self();
    }

    default Self fromString(String str) {
        read(new JSONReaderString(str));

        return self();
    }

    default Self fromPath(Path path) throws IOException {
        try (FileChannel fc = FileChannel.open(path, StandardOpenOption.READ)) {
            final ByteBuffer mapped = fc.map(MapMode.READ_ONLY, 0, fc.size());

            mapped.mark();

            final ByteReaderByteArray reader = new ByteReaderByteArray(mapped);

            if (reader.checkHeader()) {
                return read(reader);
            } else {
                mapped.reset();
                return read(new JSONReaderInputStream(new ByteBufferInputStream(mapped)));
            }
        }
    }

    default Self fromStream(InputStream in) throws IOException {
        try (BufferedInputStream bin = new BufferedInputStream(in)) {
            bin.mark(4);

            final ByteReaderInputStream reader = new ByteReaderInputStream(bin);

            if (reader.checkHeader()) {
                return read(reader);
            } else {
                bin.reset();
                return read(new JSONReaderInputStream(bin));
            }
        }
    }
}
