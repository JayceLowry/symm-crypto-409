package edu.boisestate.lowry.crypto;

/**
 * TODO
 */
public interface BlockCipher {
    /**
     * TODO Docs
     *
     * @param plaintext
     * @return
     */
    byte[] encipher(byte[] plaintext);

    /**
     * TODO Docs
     *
     * @param ciphertext
     * @return
     */
    byte[] decipher(byte[] ciphertext);

    /**
     * @return TODO
     */
    int getBlockSize();
}
