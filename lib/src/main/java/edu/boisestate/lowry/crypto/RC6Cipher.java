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
     * Creates an instance of this cipher, configured for
     * the given key size.
     *
     * @param keySize The key size for this cipher.
     */
    public RC6Cipher(KeySize keySize) {
        this.keySize = keySize;
    }

    @Override
    public byte[] encipher(byte[] plaintext, byte[] key) {
        // Validate inputs
        if (plaintext == null || key == null || plaintext.length != BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException();
        } else if (key.length != keySize.numBytes) {
            throw new UnsupportedOperationException();
        }

        int[] registers = bytesToRegisters(plaintext); // The registers [A, B, C, D]

        return new byte[0];
    }

    @Override
    public byte[] decipher(byte[] ciphertext, byte[] key) {
        // Validate inputs
        if (ciphertext == null || key == null || ciphertext.length != BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException();
        } else if (key.length != keySize.numBytes) {
            throw new UnsupportedOperationException();
        }
        // TODO Implement
        return new byte[0];
    }

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE_BYTES;
    }

    /**
     * Converts an array of bytes, assumed to be of size
     * BLOCK_SIZE_BYTES, to an array of four 32-bit registers
     * (words). Bytes are placed in a little-endian fashion.
     * @implNote We use an int array because ints are
     * always 32 bits in Java, exactly what is needed for the
     * w = 32 parameter. The registers here correspond
     * to registers A, B, C, D, and they are loaded little
     * endian so that for example the first byte is loaded
     * into the least-significant byte of A, and last byte
     * the most-significant byte of D.
     *
     * @param bytes The array of bytes.
     * @return an array of 32-bit words.
     */
    private int[] bytesToRegisters(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int[] registers = new int[NUM_REGISTERS];
        buffer.asIntBuffer().get(registers);
        return registers;
    }

    /**
     * Converts an array of registers to an array of bytes
     * in a little endian fashion, and registers is assumed
     * to be four words.
     *
     * @param registers The array of registers.
     * @return an array of bytes.
     */
    private byte[] registersToBytes(int[] registers) {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE_BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.asIntBuffer().put(registers);
        return buffer.array();
    }

    /**
     * Permute the registers to the left. This is the same
     * operation as the parallel assignment step
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
     * Permute the registers to the left. This is the same
     * operation as the parallel assignment step
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
}
