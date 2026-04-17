package edu.boisestate.lowry.crypto.benchmark;

import edu.boisestate.lowry.crypto.*;

/**
 * Benchmark for RC6 in CTR mode
 *
 * @author Jayce Lowry
 */
public class RC6CTRBenchmark extends CipherBenchmark {
    /**
     * The cipher instance.
     */
    SymmetricCipher cipher;

    @Override
    public void initCipher() {
        this.cipher = new CTRMode(new RC6Cipher(keySize));
    }

    @Override
    public byte[] performEncryption() {
        return cipher.encrypt(data, key);
    }
}
