# RCodec

RCodec; an encoder and decoder library for Java.

## Getting Started

#Encoding
CoderNode represents a JSON object, and CoderArray represents a JSON array. You can call the set() method to encode data. Be careful to supply only primitives types though. An EncoderException will be thrown if the type cannot be encoded.

Example:
```java
CoderNode node = new CoderNode();

node.set("myBool", true);
node.set("myByte", (byte) 223);
node.set("myShort", (short) 66573);
node.set("myInt", 3891);
node.set("myLong", 0x1122334455667788L);
node.set("myFloat", 10.014422f);
node.set("myDouble", Math.PI);
node.set("myString", "abcdefghijklmnopqrstuvwxyz!@#$%^&*()1234567890;''\\\"\"");
node.set("myByteArray", new byte[]{
    (byte) 'b', (byte) 'a', (byte) 's', (byte) 'e', (byte) '6', (byte) '4',
    (byte) ' ', (byte) 'a', (byte) 's', (byte) 'c', (byte) 'i', (byte) 'i'
});

node.withArray("myArray", arr -> {
    for(int i=0;i<4;i++){
        arr.add(i * i);
    }
});

// output to JSON:
node.toString();
```
Which outputs the following JSON string:
```json
{
    "myBool": true, 
    "myByte": -33, 
    "myShort": 1037, 
    "myInt": 3891, 
    "myLong": 1234605616436508552, 
    "myFloat": 10.014422, 
    "myDouble": 3.141592653589793, 
    "myString": "abcdefghijklmnopqrstuvwxyz!@#$%^&*()1234567890;''\\\"\"", 
    "myByteArray": base64(YmFzZTY0IGFzY2lp), 
    "myArray": [0, 1, 4, 9]
}
```

# Decoding
```java
CoderNode node = new CoderNode().fromString(raw_json);

boolean myBool = node.getBoolean("boolValue").orElseThrow(() -> new RuntimeException("Value was not found!"));
```
The type Optional is returned, so you can handle missing data.


There's also a shorter notation:
```java
node.ifDouble("myDouble", System.out::println);
```

You can also open json objects and arrays when decoding with ifNode and ifArray and they will be skipped if they aren't provided.
```java
node.ifNode("myNode", myNode -> myNode
    .ifDouble("myDouble", System.out::println)
    .ifDouble("anotherDouble", System.out::println)
);

node.ifArray("myArray", myArray -> myArray
    .ifDouble(0, System.out::println)
    .ifDouble(1, System.out::println)
);
```

# Compacting data
You can additionally pack your data into smaller filesizes by minimizing the JSON, or by encoding to a binary format.

You can use BeautifyRules in order to generate the minified string:
```java
CoderCode node = new CoderNoder();
// write data into "node"
...

// get output
node.withBeautify(BeautifyRules.MINIFIED).toString();
```

Calling toBytes() will encode as the binary format:
```java
CoderCode node = new CoderNoder();
// write data into "node"
...

// get output
node.toBytes();
```

To decode from bytes, you muse call fromBytes();
```java
CoderCode node = new CoderNoder().fromBytes(my_bytes);
```

## Authors

* **Robert Hewitt** - *Project Manager* - [Runouw](https://github.com/runouw)
* **Zachary Michaels** - *Code Review and Documentation*

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details