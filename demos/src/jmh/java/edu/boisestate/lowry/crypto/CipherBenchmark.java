package edu.boisestate.lowry.crypto;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.security.SecureRandom;
import java.util.Random;

/**
 * A benchmark for key sizes 128, 192, and 256 bits.
 * Three data sizes are also tested; 1KB, 1MB, and 10MB.
 *
 * @author Jayce Lowry
 */
@State(Scope.Benchmark)
public abstract class CipherBenchmark {
    /**
     * Key sizes to test
     */
    @Param({"BITS_128", "BITS_192", "BITS_256"})
    public KeySize keySize;
    /**
     * Data sizes to test (1KB, 1MB, 10MB)
     */
    @Param({"1024", "1048576", "10485760"})
    public int dataSize;
    /**
     * The key used for all encryptions.
     */
    protected byte[] key;
    /**
     * The data to encrypt.
     */
    protected byte[] data;

    /**
     * Initializes random data, key, and the cipher.
     */
    @Setup(Level.Trial)
    public void setup() {
        data = new byte[dataSize];
        new Random().nextBytes(data);
        key = new byte[keySize.numBytes];
        new SecureRandom().nextBytes(key);
        initCipher();
    }

    /**
     * Tests the throughput for the cipher.
     *
     * @param bh Consumer to prevent JIT optimization.
     */
    @Benchmark
    public void testThroughput(Blackhole bh) {
        bh.consume(performEncryption());
    }

    /**
     * Initializes the cipher being tested.
     */
    public abstract void initCipher();

    /**
     * Calls the specific encrypt operation for the cipher.
     *
     * @return The ciphertext.
     */
    public abstract byte[] performEncryption();
}
