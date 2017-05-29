# RCodec

RCodec; an encoder and decoder library for Java.

## Getting Started

To encode:
```java

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

To decode:
```java

CoderNode node = new CoderNode().fromString(raw_json);

boolean myBool = node.getBoolean("boolValue").orElseThrow(() -> new Error());
node.getByte("byteValue").orElseThrow(() -> new Error("Value wasn't found!"));
...
```

The type Optional is returned, so you can handle missing data. There's also a shorter notation:
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


## Authors

* **Robert Hewitt** - *Project Manager* - [Runouw](https://github.com/runouw)
* **Zachary Michaels** - *Code Review and Documentation*

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details