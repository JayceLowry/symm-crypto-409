package edu.boisestate.lowry.crypto;

/**
 * Defines a cryptographic block cipher. A BlockCipher operates on a
 * fixed-length block of data. BlockCipher is deterministic, that is
 * given the same key and block, it will produce the same output.
 *
 * @author Jayce Lowry
 */
public interface BlockCipher {
    /**
     * Enciphers a fixed-length block of plaintext.
     *
     * @param plaintext A block of data with length equivalent to getBlockSize().
     * @param key The secret key for enciphering.
     * @return The resulting enciphered block.
     * @throws IllegalArgumentException If the input length does not match the block size.
     */
    byte[] encipher(byte[] plaintext, byte[] key);

    /**
     * Deciphers a fixed-length block of ciphertext.
     *
     * @param ciphertext A block of encrypted data with length equivalent to getBlockSize().
     * @param key The secret key for deciphering.
     * @return The resulting deciphered block.
     * @throws IllegalArgumentException If the input length does not match the block size.
     */
    byte[] decipher(byte[] ciphertext, byte[] key);

    /**
     * Returns the fixed block size this cipher operates on.
     *
     * @return The block size in bytes.
     */
    int getBlockSize();
}
