# RCodec

RCodec; an encoder and decoder library for Java.

## Getting Started

To encode:
```java

CoderNode node = new CoderNode();
        
boolean boolValue = true;
byte byteValue = (byte) 223;
short shortValue = (short) 66573;
int intValue = 3891;
long longValue = 0x1122334455667788L;
float floatValue = 10.014422f;
double doubleValue = Math.PI;
String stringValue = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()1234567890;''\\\"\"";
byte[] byteArrayValue = new byte[]{
    (byte) 'b', (byte) 'a', (byte) 's', (byte) 'e', (byte) '6', (byte) '4',
    (byte) ' ', (byte) 'a', (byte) 's', (byte) 'c', (byte) 'i', (byte) 'i'
};


node.set("boolValue", boolValue);
node.set("byteValue", byteValue);
node.set("shortValue", shortValue);
node.set("intValue", intValue);
node.set("longValue", longValue);
node.set("floatValue", floatValue);
node.set("doubleValue", doubleValue);
node.set("stringValue", stringValue);
node.set("byteArrayValue", byteArrayValue);

// output to JSON:
node.toString();
```

To decode:
```java

CoderNode node = new CoderNode().fromString(raw_json);

boolean myBool = node.getBoolean("boolValue").orElseThrow(() -> new Error());
node.getByte("byteValue").orElseThrow(() -> new Error());
...
```



## Authors

* **Robert Hewitt** - *Project Manager* - [Runouw](https://github.com/runouw)
* **Zachary Michaels** - *Code Review and Documentation*

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details