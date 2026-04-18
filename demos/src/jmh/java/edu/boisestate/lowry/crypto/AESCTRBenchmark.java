package edu.boisestate.lowry.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Benchmark for BouncyCastle's AES implementation in
 * CTR mode.
 *
 * @author Jayce Lowry.
 */
public class AESCTRBenchmark extends CipherBenchmark {
    /**
     * The cipher instance.
     */
    private Cipher cipher;

    @Override
    public void initCipher() {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        try {
            SecretKeySpec secretKey = new SecretKeySpec(this.key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);

            cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        } catch (Exception e) {
            throw new RuntimeException("AES Init Failed for " + keySize, e);
        }
    }

    @Override
    public byte[] performEncryption() {
        try {
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
