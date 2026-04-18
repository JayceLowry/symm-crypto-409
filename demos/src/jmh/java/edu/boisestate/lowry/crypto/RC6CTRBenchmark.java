package edu.boisestate.lowry.crypto;

/**
 * Benchmark for RC6 in CTR mode
 *
 * @author Jayce Lowry
 */
public class RC6CTRBenchmark extends CipherBenchmark {
    /**
     * The cipher instance.
     */
    private CTRMode cipher;
    /**
     * Initial counter.
     */
    private byte[] iv;

    @Override
    public void initCipher() {
        RC6Cipher rc6 = new RC6Cipher(keySize);
        rc6.initKey(key);
        this.cipher = new CTRMode(rc6);
        iv = new byte[rc6.getBlockSize()];
    }

    @Override
    public byte[] performEncryption() {
        cipher.runCTR(data, key, iv);
        return data;
    }
}
