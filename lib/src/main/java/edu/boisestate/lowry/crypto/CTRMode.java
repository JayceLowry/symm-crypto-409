package edu.boisestate.lowry.crypto;

/**
 * TODO
 */
public class CTRMode implements SymmetricCipher {
    /**
     * TODO Docs
     *
     * @param cipher
     * @param iv
     */
    public CTRMode(BlockCipher cipher, byte[] iv) {

    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        return new byte[0];
    }
}
