/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.rcodec;

import com.runouw.rcodec.DecodeException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Wrapper for a generic piece of data along with a Type the data is associated
 * with.
 * @author rhewitt - Originator
 * @author zmichaels - Maintainer, Documentation
 */
public final class CoderData {

    // If enhanced enums ever get made, consider them: http://openjdk.java.net/jeps/301
    
    public enum Type {
        BOOLEAN(
                Boolean.class,
                (byte) 'b',
                (writer, val) -> {
                    writer.write((byte) 'b');
                    writer.writeBoolean(val);
                },
                (reader) -> reader.readBoolean(),
                (writer, val) -> writer.append(val),
                null
        ),
        BYTE(
                Byte.class,
                (byte) '1',
                (writer, val) -> {
                    writer.write((byte) '1');
                    writer.write((byte) val);
                },
                (reader) -> reader.readByte(),
                (writer, val) -> writer.append(val),
                null
        ),
        SHORT(
                Short.class,
                (byte) '2',
                (writer, val) -> {
                    writer.write((byte) '2');
                    writer.writeShort(val);
                },
                (reader) -> reader.readShort(),
                (writer, val) -> writer.append(val),
                null
        ),
        INT(
                Integer.class,
                (byte) 'i',
                (writer, val) -> {
                    writer.write((byte) 'i');
                    writer.writeInt(val);
                },
                (reader) -> reader.readInt(),
                (writer, val) -> writer.append(val),
                null
        ),
        LONG(
                Long.class,
                (byte) 'l',
                (writer, val) -> {
                    writer.write((byte) 'l');
                    writer.writeLong(val);
                },
                (reader) -> reader.readLong(),
                (writer, val) -> writer.append(val),
                null
        ),
        FLOAT(
                Float.class,
                (byte) 'f',
                (writer, val) -> {
                    writer.write((byte) 'f');
                    writer.writeFloat(val);
                },
                (reader) -> reader.readFloat(),
                (writer, val) -> writer.append(val),
                null
        ),
        DOUBLE(
                Double.class,
                (byte) 'd',
                (writer, val) -> {
                    writer.write((byte) 'd');
                    writer.writeDouble(val);
                },
                (reader) -> reader.readDouble(),
                (writer, val) -> writer.append(val),
                null
        ),
        STRING(
                String.class,
                (byte) 's',
                (writer, val) -> {
                    writer.write('s');
                    writer.writeString(val);
                    writer.write('\0');
                },
                (reader) -> reader.readString(),
                (writer, val) -> writer.appendQuotedAddSlashes(val),
                null //(reader) -> reader.readQuotedRemoveSlashes()
        ),
        BYTES(
                byte[].class,
                (byte) '!',
                (writer, val) -> {
                    writer.write('!');
                    writer.writeInt(val.length);
                    writer.write(val);
                },
                (reader) -> {
                    int length = reader.readInt();
                    return reader.readBytes(length);
                },
                (writer, val) -> {
                    writer.append("base64(");
                    writer.append(Base64.getEncoder().encodeToString(val));
                    writer.append(")");
                },
                null
        ),
        NODE(
                CoderNode.class,
                (byte) '{',
                (writer, val) -> val.write(writer),
                (reader) -> new CoderNode().read(reader, false),
                (writer, val) -> val.write(writer),
                (reader) -> new CoderNode().read(reader, false)
        ),
        ARRAY(
                CoderArray.class,
                (byte) '[',
                (writer, val) -> val.write(writer),
                (reader) -> new CoderArray().read(reader, false),
                (writer, val) -> val.write(writer),
                (reader) -> new CoderArray().read(reader, false)
        ),
        NULL(
                null,
                (byte) '_',
                (writer, val) -> writer.write((byte) '_'),
                reader -> null,
                (writer, val) -> writer.append("null"),
                reader -> null
        );
        private final Class<?> cl;

        @SuppressWarnings("rawtypes")
        private final BiConsumer toBytes;

        @SuppressWarnings("rawtypes")
        private final BiConsumer toString;

        @SuppressWarnings("rawtypes")
        private final Function parseBytes;

        @SuppressWarnings("rawtypes")
        private final Function parseString;

        private final byte byteValue;

        private <T> Type(Class<T> cl,
                byte byteValue,
                BiConsumer<ByteWriter, T> toBytes,
                Function<ByteReader, T> parseBytes,
                BiConsumer<JSONWriter, T> toString,
                Function<JSONReader, T> parseString
        ) {
            this.cl = cl;
            this.byteValue = byteValue;
            this.toBytes = toBytes;
            this.parseBytes = parseBytes;

            this.toString = toString;
            this.parseString = parseString;
        }

        public byte getByteValue() {
            return byteValue;
        }

        @SuppressWarnings("unchecked")
        CoderData decode(ByteReader reader) {
            return new CoderData(this, parseBytes.apply(reader));
        }

        @SuppressWarnings("unchecked")
        CoderData decode(JSONReader reader) {
            return new CoderData(this, parseString.apply(reader));
        }

        private static final Map<Class<?>, Type> CLASS_TO_TYPE = new HashMap<>();
        private static final Map<Byte, Type> BYTE_TO_TYPE = new HashMap<>();

        static {
            for (Type type : Type.values()) {
                CLASS_TO_TYPE.put(type.cl, type);
                BYTE_TO_TYPE.put(type.byteValue, type);
            }
        }

        public static Optional<Type> fromClass(Class<?> cl) {
            return Optional.ofNullable(CLASS_TO_TYPE.get(cl));
            // Objects.requireNonNull(CLASS_TO_TYPE.get(cl), () -> "Class '" + cl.getName() + "' not supported!")
        }

        public static Optional<Type> fromByte(byte b) {
            return Optional.ofNullable(BYTE_TO_TYPE.get(b));
            // Objects.requireNonNull(BYTE_TO_TYPE.get(b), () -> "Byte '" + ((char) b) + "' not supported!");
        }

    }
    private final Type type;

    private final Object data;

    public CoderData(Type type, Object data) {
        this.type = type;

        if (data == null) {
            type = Type.NULL;
        }

        if (type == Type.NULL) {
            this.data = null;
        } else {
            this.data = type.cl.cast(data);
        }
    }

    public Object get() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCast(Class<T> cl) throws ClassCastException {
        if (data == null || type.cl.equals(cl)) {
            return (T) data;
        }

        switch (type) {
            case BOOLEAN:
                return fromBoolean(cl, (boolean) data);
            case BYTE:
                return fromByte(cl, (byte) data);
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                return fromNumber(cl, (Number) data);
            case STRING:
                return fromString(cl, (String) data);
            case NULL:
                return null;
            case BYTES:
            case NODE:
            case ARRAY:
                throw new ClassCastException("Can't cast " + type + " to " + cl.getSimpleName());
        }

        return cl.cast(data);
    }

    private <T> T fromBoolean(Class<T> cl, boolean value) {
        return fromNumber(cl, value ? 1 : 0);
    }

    private <T> T fromByte(Class<T> cl, byte value) {
        return fromNumber(cl, (int) value);
    }

    @SuppressWarnings("unchecked")
    private <T> T fromNumber(Class<T> cl, Number value) {
        switch (cl.getSimpleName()) {
            case "Byte":
                return (T) Byte.valueOf(value.byteValue());
            case "Short":
                return (T) Short.valueOf(value.shortValue());
            case "Integer":
                return (T) Integer.valueOf(value.intValue());
            case "Long":
                return (T) Long.valueOf(value.longValue());
            case "Float":
                return (T) Float.valueOf(value.floatValue());
            case "Double":
                return (T) Double.valueOf(value.doubleValue());
            case "String":
                return (T) value.toString();
        }
        throw new ClassCastException("Can't cast " + type + " to " + cl.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T> T fromString(Class<T> cl, String value) {
        switch (cl.getSimpleName()) {
            case "Boolean":
                return (T) (Boolean) value.toLowerCase().equals("true");
            case "Byte":
                return (T) Byte.valueOf(value);
            case "Short":
                return (T) Short.valueOf(value);
            case "Integer":
                return (T) Integer.valueOf(value);
            case "Long":
                return (T) Long.valueOf(value);
            case "Float":
                return (T) Float.valueOf(value);
            case "Double":
                return (T) Double.valueOf(value);
        }
        throw new ClassCastException("Can't cast " + type + " to " + cl.getSimpleName());
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return write(new JSONWriterStringBuilder()).toString();
    }

    @SuppressWarnings("unchecked")
    JSONWriter write(JSONWriter writer) {
        type.toString.accept(writer, data);
        return writer;
    }

    @SuppressWarnings("unchecked")
    ByteWriter write(ByteWriter writer) {
        type.toBytes.accept(writer, data);
        return writer;
    }

    public byte[] toBytes() {
        ByteWriterByteArray b = new ByteWriterByteArray();
        write(b);
        return b.toBytes();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.type);
        hash = 73 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoderData other = (CoderData) obj;

        boolean aStringyType = !(this.type == Type.ARRAY || this.type == Type.NODE || this.type == Type.BYTES);
        boolean bStringyType = !(other.type == Type.ARRAY || this.type == Type.NODE || this.type == Type.BYTES);

        if (aStringyType && bStringyType) {
            return Objects.equals(this.getCast(String.class), other.getCast(String.class));
        }

        if (this.type != other.type) {
            return false;
        }

        if (this.type == Type.BYTES && other.type == Type.BYTES) {
            return Arrays.equals((byte[]) this.data, (byte[]) other.data);
        }

        return Objects.equals(this.data, other.data);
    }

    static CoderData fromJSONToken(JSONTokenizer.Token token, JSONReader reader) {
        switch (token.type) {
            case START_BRACE:
                return Type.NODE.decode(reader);
            case START_BRACKET:
                return Type.ARRAY.decode(reader);
            case PROPERTY:
                return fromJSONProperty(token.buf);
            case STRING:
                return fromQuotedString(token.buf);
            default:
                throw new DecodeException("Cannot make a CoderData from " + token);
        }
    }

    static CoderData fromJSONProperty(String data) {
        if (data.startsWith("base64(")) {
            String base64 = data.substring("base64(".length(), data.length() - 1);

            return new CoderData(Type.BYTES, Base64.getDecoder().decode(base64));
        }
        if (data.equals("null")) {
            return new CoderData(Type.NULL, null);
        }

        boolean hasDecimal = data.indexOf('.') != -1;
        try {
            if (hasDecimal) {
                Double b = Double.parseDouble(data);
                return new CoderData(Type.DOUBLE, b);
            } else {
                Long a = Long.parseLong(data);
                return new CoderData(Type.LONG, a);
            }
        } catch (NumberFormatException ex) {
            // give up and try a string
            return new CoderData(Type.STRING, data);
        }
    }

    static CoderData fromQuotedString(String data) {
        return new CoderData(Type.STRING, data);
    }

}
