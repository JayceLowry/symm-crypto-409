package edu.boisestate.lowry.crypto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * An implementation of the RC6-32/20/b block cipher. This implementation
 * is configured for word size w = 32 and r = 20 rounds. It also supports
 * variable secret key lengths 'b' (16, 24, or 32 bytes), corresponding to
 * requirements for the AES-128, AES-192, and AES-256.
 *
 * @author Jayce Lowry
 */
public class RC6Cipher implements BlockCipher {
    /**
     * The block size in bytes. In RC6, the block size is defined as
     * four words (4w). For w = 32 bits (4 bytes), the block size is
     * 16 bytes (128 bits).
     */
    private static final int BLOCK_SIZE_BYTES = 16;
    /**
     * The number of registers.
     */
    private static final int NUM_REGISTERS = 4;
    /**
     * The number of rounds for encryption and decryption.
     */
    private static final int NUM_ROUNDS = 20;
    /**
     * The key size, corresponding to the RC6 'b' parameter.
     */
    private final KeySize keySize;
    /**
     * The round keys S[0, ..., 2r + 3].
     */
    private int[] roundKeys;

    /**
     * Creates an instance of this cipher, configured for
     * the given key size.
     *
     * @param keySize The key size for this cipher.
     */
    public RC6Cipher(KeySize keySize) {
        this.keySize = keySize;
        roundKeys = null;
    }

    @Override
    public byte[] encipher(byte[] plaintext, byte[] key) {
        // Validate inputs
        if (plaintext == null || plaintext.length != BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException();
        }
        // Initialize round keys if necessary and load plaintext into registers
        if (roundKeys == null) {
            roundKeys = keySchedule(key);
        }
        int[] registers = bytesToRegisters(plaintext); // The registers [A, B, C, D]

        // Pre-whitening
        registers[1] = registers[1] + roundKeys[0];
        registers[3] = registers[3] + roundKeys[1];

        // Run r rounds of encryption
        for (int i = 1; i <= NUM_ROUNDS; i++) {
            int t = Integer.rotateLeft(registers[1] * (2 * registers[1] + 1), 5); // Rotate a distance lg 32 = 5
            int u = Integer.rotateLeft(registers[3] * (2 * registers[3] + 1), 5);
            registers[0] = Integer.rotateLeft(registers[0] ^ t, u) + roundKeys[2 * i];
            registers[2] = Integer.rotateLeft(registers[2] ^ u, t) + roundKeys[2 * i + 1];
            rotateRegistersLeft(registers);
        }
        // Post-whitening
        registers[0] = registers[0] + roundKeys[2 * NUM_ROUNDS + 2];
        registers[2] = registers[2] + roundKeys[2 * NUM_ROUNDS + 3];

        return registersToBytes(registers);
    }

    @Override
    public byte[] decipher(byte[] ciphertext, byte[] key) {
        // Validate inputs
        if (ciphertext == null || ciphertext.length != BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException();
        } else if (key.length != keySize.numBytes) {
            throw new UnsupportedOperationException();
        }
        // Initialize round keys if necessary and load ciphertext into registers
        if (roundKeys == null) {
            roundKeys = keySchedule(key);
        }
        int[] registers = bytesToRegisters(ciphertext); // The registers [A, B, C, D]

        registers[2] = registers[2] - roundKeys[2 * NUM_ROUNDS + 3];
        registers[0] = registers[0] - roundKeys[2 * NUM_ROUNDS + 2];

        // Run r rounds of decryption
        for (int i = NUM_ROUNDS; i >= 1; i--) {
            rotateRegistersRight(registers);
            int u = Integer.rotateLeft(registers[3] * (2 * registers[3] + 1), 5);
            int t = Integer.rotateLeft(registers[1] * (2 * registers[1] + 1), 5);
            registers[2] = Integer.rotateRight(registers[2] - roundKeys[2 * i + 1], t) ^ u;
            registers[0] = Integer.rotateRight(registers[0] - roundKeys[2 * i], u) ^ t;
        }
        registers[3] = registers[3] - roundKeys[1];
        registers[1] = registers[1] - roundKeys[0];
        return registersToBytes(registers);
    }

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE_BYTES;
    }

    /**
     * Preemptively calls the key schedule with the given key
     * for the purpose of performing bulk encryption. A call
     * to this will cause they key parameter for encipher()
     * and decipher() to be ignored.
     *
     * @param key The key to initialize.
     */
    public void initKey(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException();
        } else if (key.length != keySize.numBytes) {
            throw new UnsupportedOperationException();
        }
        this.roundKeys = keySchedule(key);
    }

    /**
     * Resets the stored key to nothing. A call to this
     * will cause the key parameter for encipher() and
     * decipher() to be usable following a call to
     * initKey().
     */
    public void resetKey() {
        this.roundKeys = null;
    }

    /**
     * Converts an array of bytes to an array of 32-bit
     * registers (words). Bytes are placed in a little-endian
     * fashion. The length of bytes is assumed to be a multiple
     * of four.
     * @implNote We use an int array because ints are always
     * 32 bits in Java, exactly what is needed for the
     * w = 32 parameter. Loading the registers little
     * endian corresponds to, for example, how the placement
     * of bytes into A, B, C, D is described, and the same
     * for the array L in the key schedule.
     *
     * @param bytes The array of bytes.
     * @return an array of 32-bit registers.
     */
    private int[] bytesToRegisters(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int[] registers = new int[bytes.length / Integer.BYTES];
        buffer.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(registers);
        return registers;
    }

    /**
     * Converts an array of registers to an array of bytes
     * in a little endian fashion.
     *
     * @param registers The array of registers.
     * @return an array of bytes.
     */
    private byte[] registersToBytes(int[] registers) {
        ByteBuffer buffer = ByteBuffer.allocate(registers.length * Integer.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().put(registers);
        return buffer.array();
    }

    /**
     * Rotate/permute the registers to the left. This is
     * the same operation as the parallel assignment step
     * (A, B, C, D) = (B, C, D, A) for enciphering.
     *
     * @param registers The registers.
     */
    private void rotateRegistersLeft(int[] registers) {
        int tmp = registers[0];
        for (int i = 0; i < NUM_REGISTERS - 1; i ++) {
            registers[i] = registers[i + 1];
        }
        registers[NUM_REGISTERS - 1] = tmp;
    }

    /**
     * Rotate/permute the registers to the left. This is
     * the same operation as the parallel assignment step
     * (A, B, C, D) = (D, A, B, C) for deciphering.
     *
     * @param registers The registers.
     */
    private void rotateRegistersRight(int[] registers) {
        int tmp = registers[NUM_REGISTERS - 1];
        for (int i = NUM_REGISTERS - 1; i > 0; i--) {
            registers[i] = registers[i - 1];
        }
        registers[0] = tmp;
    }

    /**
     * Derives 2r + 4 round keys from the given key, loaded
     * into an array of 32-bit registers.
     *
     * @param key The private key.
     * @return An array of the round keys as registers.
     */
    private int[] keySchedule(byte[] key) {
        // Magic constants
        final int P = 0xB7E15163;
        final int Q = 0x9E3779B9;

        // Load the key into a set of registers
        int[] keyRegisters = bytesToRegisters(key); // The array L
        int numRoundKeys = 2 * NUM_ROUNDS + 4;
        int[] roundKeys = new int[numRoundKeys]; // The array S

        // Initialize S with the sequence P, P + Q, P + 2Q, ..., P + (2r + 3)Q
        roundKeys[0] = P;
        for (int i = 1; i < numRoundKeys; i++) {
            roundKeys[i] = roundKeys[i - 1] + Q;
        }

        // Mix the key registers L into the round key array S
        int i, j, A, B;
        i = j = A = B = 0;
        int iterations = 3 * Math.max(keyRegisters.length, numRoundKeys);

        for (int s = 1; s <= iterations; s++) {
            roundKeys[i] = Integer.rotateLeft(roundKeys[i] + (A + B), 3);
            A = roundKeys[i];

            keyRegisters[j] = Integer.rotateLeft(keyRegisters[j] + (A + B), (A + B));
            B = keyRegisters[j];

            i = (i + 1) % numRoundKeys;
            j = (j + 1) % keyRegisters.length;
        }

        return roundKeys;
    }
}
