package edu.boisestate.lowry.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 * An implementation of the Counter (CTR) mode of operation.
 *
 * @author Jayce Lowry.
 */
public class CTRMode implements SymmetricCipher {
    /**
     * The underlying block cipher used.
     */
    private final BlockCipher blockCipher;
    /**
     * The underlying block cipher's block size.
     */
    private final int blockSize;

    /**
     * Creates a CTRMode instance with the given block
     * cipher as the underlying cipher.
     *
     * @param cipher The underlying block cipher for this instance.
     */
    public CTRMode(BlockCipher cipher) {
        this.blockCipher = cipher;
        blockSize = cipher.getBlockSize();
    }

    @Override
    public byte[] encrypt(byte[] plaintext, byte[] key) {
        // Randomly generate a nonce
        SecureRandom rand = new SecureRandom();
        byte[] nonce = new byte[blockSize / 2];
        rand.nextBytes(nonce);
        // Prepare the counter with the nonce as the first half
        byte[] counter = new byte[blockSize];
        System.arraycopy(nonce, 0, counter, 0, nonce.length);
        // Encrypt
        byte[] ciphertext = runCTR(plaintext, key, counter);
        // Prepend nonce to the ciphertext
        ByteBuffer buffer = ByteBuffer.allocate(nonce.length + ciphertext.length);
        buffer.put(nonce).put(ciphertext);
        return buffer.array();
    }

    @Override
    public byte[] decrypt(byte[] ciphertext, byte[] key) {
        // Separate nonce and data
        ByteBuffer buffer = ByteBuffer.wrap(ciphertext);
        byte[] nonce = new byte[blockSize / 2];
        byte[] encryptedData = new byte[ciphertext.length - (blockSize / 2)];
        buffer.get(nonce).get(encryptedData);
        // Prepare counter
        byte[] counter = new byte[blockSize];
        System.arraycopy(nonce, 0, counter, 0, nonce.length);
        // Decrypt
        return runCTR(encryptedData, key, counter);
    }

    /**
     * Runs the CTR operation on the given text, using a secret key
     * and initial counter.
     *
     * @param text The plaintext or ciphertext.
     * @param key The secret key.
     * @param counter The initial counter.
     * @return The resulting processed text.
     */
    private byte[] runCTR(byte[] text, byte[] key, byte[] counter) {
        // Copy data into a working output array
        byte[] out = new byte[text.length];
        System.arraycopy(text, 0, out, 0, text.length);

        // XOR text data with the encrypted counter. Once each position
        // of the current counter has been seen, re-encrypt it and increment.
        byte[] encryptedCounter = new byte[0];
        int j = blockSize;
        for (int i = 0; i < text.length; i++) {
            if (j == blockSize) {
                encryptedCounter = blockCipher.encipher(counter, key);
                incrementBlock(counter, blockSize / 2, counter.length);
                j = 0;
            }
            out[i] ^= encryptedCounter[j];
            j++;
        }
        return out;
    }

    /**
     * Increments a specific sub-block of the given bytes, treating it as an
     * unsigned integer (big endian). This is essentially a ripple-carry addition
     * of the sub-block and 1.
     *
     * @param input The input block containing the sub-block to increment.
     * @param start The index of the most significant byte of the sub-block, inclusive.
     * @param end The index of the least significant byte of the sub-block, exclusive.
     * @throws IllegalArgumentException If the input is null, the indices are out of bounds,
     * or the ending index is before the starting index.
     * @throws IllegalStateException If the entire block overflows.
     */
    protected void incrementBlock(byte[] input, int start, int end) {
        if (input == null || start < 0 || end > input.length || end < start) {
            throw new IllegalArgumentException();
        } else if (start == end) {
            return;
        }
        int i;
        for (i = end - 1; i >= start; i--) {
            input[i]++;
            if (input[i] != 0) { // No carry
                break;
            }
            // Carry
        }
        if (i < start) { // Overflow
            throw new IllegalStateException();
        }
    }
}
