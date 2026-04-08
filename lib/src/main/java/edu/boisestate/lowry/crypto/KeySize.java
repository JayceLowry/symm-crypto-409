package edu.boisestate.lowry.crypto;

/**
 * Represents the size for a symmetric secret key. Currently
 * only 128, 192, and 256-bit key sizes are supported.
 *
 * @author Jayce Lowry
 */
public enum KeySize {
    /** 128-bit (16-byte) key */
    BITS_128(16),
    /** 192-bit (24-byte) key */
    BITS_192(24),
    /** 256-bit (32-byte) key */
    BITS_256(32);

    /**
     * The key size in bytes.
     */
    public final int numBytes;

    /**
     * Internal constructor for KeySize presets.
     *
     * @param numBytes The number of bytes for the key.
     */
    KeySize(int numBytes) {
        this.numBytes = numBytes;
    }
}
