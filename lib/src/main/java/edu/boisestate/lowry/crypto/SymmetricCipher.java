package edu.boisestate.lowry.crypto;

/**
 * Defines a symmetric-key encryption/decryption scheme. A SymmetricCipher is
 * a high-level implementation of a mode of operation that uses an underlying
 * BlockCipher. Implementations of this interface are expected to handle
 * arbitrary-length inputs.
 *
 * @author Jayce Lowry
 */
public interface SymmetricCipher {
    /**
     * Encrypts the provided plaintext using a symmetric key.
     *
     * @param plaintext The data to be encrypted.
     * @param key The secret key for encryption.
     * @return The resulting ciphertext.
     * @throws IllegalArgumentException If the key length is invalid for the underlying cipher.
     */
    byte[] encrypt(byte[] plaintext, byte[] key);

    /**
     * Decrypts the provided ciphertext using a symmetric key.
     *
     * @param ciphertext The encrypted data to be decrypted.
     * @param key The secret key for decryption.
     * @return The resulting plaintext.
     * @throws IllegalArgumentException If the key length is invalid for the underlying cipher.
     */
    byte[] decrypt(byte[] ciphertext, byte[] key);
}
