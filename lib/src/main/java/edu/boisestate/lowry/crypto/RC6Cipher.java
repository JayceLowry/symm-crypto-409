package edu.boisestate.lowry.crypto;

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
     * The key size, corresponding to the RC6 'b' parameter.
     */
    private final KeySize keySize;
    /**
     * An array representation for registers A, B, C, D.
     */
    private int[] registers = new int[4];

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
        // TODO Implement
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
}
