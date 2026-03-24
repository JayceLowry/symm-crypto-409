package edu.boisestate.lowry.crypto;

/**
 * TODO
 */
public interface SymmetricCipher {
    /**
     * TODO Docs
     *
     * @param plaintext
     * @return
     */
    byte[] encrypt(byte[] plaintext);

    /**
     * TODO Docs
     *
     * @param ciphertext
     * @return
     */
    byte[] decrypt(byte[] ciphertext);
}
