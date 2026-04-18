# RC6-CTR Implementation

---

## Overview

This is a java implementation of the RC6 algorithm in CTR mode.

## Navigating the Project

**Source code quick links**
- [Core implementation](https://github.com/JayceLowry/symm-crypto-409/tree/main/lib/src/main/java/edu/boisestate/lowry/crypto):
This directory contains all files related to the library implementation.
- [Unit tests](https://github.com/JayceLowry/symm-crypto-409/tree/main/lib/src/test/java/edu/boisestate/lowry/crypto): 
This directory contains tests for `RC6Cipher` and `CTRMode`. 
- [Demo](https://github.com/JayceLowry/symm-crypto-409/blob/main/demos/src/main/java/edu/boisestate/lowry/crypto/demo/CipherDemo.java):
This shows an example program that uses the implementation to encrypt and decrypt 
a simple message.
- [Benchmarking](https://github.com/JayceLowry/symm-crypto-409/tree/main/demos/src/jmh/java/edu/boisestate/lowry/crypto): This directory contains the programs used to benchmark the
RC6-CTR implementation and the BouncyCastle AES-CTR mode implementation for comparison,
using the Java Microbenchmark Harness.

**Project documentation**

To view the library documentation in a more readable web interface, please visit
this [Project Documentation Site](https://jaycelowry.github.io/symm-crypto-409/).

## Library Files

**The Interfaces**
- `BlockCipher.java`: Defines how a block cipher should behave.
- `SymmetricCipher.java`: Defines the encryption/decryption processes for
arbitrary-length data.

**The Implementations**
- `RC6Cipher.java`: Contains the implementation of the RC6 algorithms.
- `CTRMode.java`: Contains the logic for CTR mode.
- `KeySize.java`: Defines the supported key sizes.

##  Compiling and Using

Compiling this project requires the Java Development Kit (JDK) 21 or higher.
This project uses Gradle, which includes a "wrapper" (`gradlew` or `gradlew.bat`) 
to handle the build process. These should be run in a terminal from the project
root.

**Tests**:
```sh
./gradlew test    # Mac/Linux
gradlew.bat test  # Windows
```
**Demo**:
```sh
./gradlew run    # Mac/Linux
gradlew.bat run  # Windows
```
**Benchmark**:
```sh
./gradlew jmh    # Mac/Linux
gradlew.bat jmh  # Windows
```
