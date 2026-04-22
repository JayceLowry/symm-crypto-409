package edu.boisestate.lowry.crypto;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tester for RC6Cipher, including Known Answer
 * Tests (KATs).
 *
 * @author Jayce Lowry
 */
public class RC6Test {

    /**
     * Runs RC6 encryption and decryption for each test
     * vector in the rc6_test_vectors.csv file.
     *
     * @return The tests.
     * @throws Exception If an I/O error occurs.
     */
    @TestFactory
    Collection<DynamicTest> knownAnswerTests() throws Exception {
        List<DynamicTest> tests = new ArrayList<>();

        InputStream is = getClass().getResourceAsStream("/rc6_test_vectors.csv");
        assertNotNull(is, "Failed to read from test vector file");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        reader.readLine();

        String line;
        int lineNum = 1;
        while ((line = reader.readLine()) != null) {
            lineNum++;
            if (line.trim().isEmpty()) {
                continue;
            }

            final String[] cols = line.split(",");
            String label = "Line " + lineNum + " (" + cols[0] + ")";

            tests.add(DynamicTest.dynamicTest(label, () -> {
                KeySize size = KeySize.valueOf(cols[0]);
                byte[] key = HexFormat.of().parseHex(cols[1]);
                byte[] plaintext = HexFormat.of().parseHex(cols[2]);
                byte[] ciphertext = HexFormat.of().parseHex(cols[3]);

                RC6Cipher cipher = new RC6Cipher(size);
                byte[] ciphertextResult = cipher.encipher(plaintext, key);
                byte[] plaintextResult = cipher.decipher(ciphertextResult, key);

                assertEquals(HexFormat.of().formatHex(ciphertext), HexFormat.of().formatHex(ciphertextResult), "Ciphertext mismatch at " + label);
                assertEquals(HexFormat.of().formatHex(plaintext), HexFormat.of().formatHex(plaintextResult), "Plaintext mismatch at " + label);
            }));
        }

        return tests;
    }

    /**
     * Helper for testing invalid block sizes.
     *
     * @param size The key size for the cipher to test.
     */
    private void invalidBlockTestHelper(KeySize size) {
        RC6Cipher cipher = new RC6Cipher(size);
        byte[] key = new byte[size.numBytes];
        byte[] invalidBlockA = new byte[0];
        byte[] invalidBlockB = new byte[cipher.getBlockSize() / 2];
        byte[] invalidBlockC = new byte[cipher.getBlockSize() + 4];

        assertThrows(IllegalArgumentException.class, () -> {
            cipher.encipher(invalidBlockA, key);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cipher.encipher(invalidBlockB, key);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cipher.encipher(invalidBlockC, key);
        });
    }

    /**
     * Helper for testing invalid key sizes.
     *
     * @param size The correct key size for the cipher to test.
     */
    private void invalidKeySizeHelper(KeySize size) {
        RC6Cipher cipher = new RC6Cipher(size);
        byte[] block = new byte[cipher.getBlockSize()];
        byte[] invalidKeyA = new byte[0];
        byte[] invalidKeyB = new byte[size.numBytes - 1];
        byte[] invalidKeyC = new byte[size.numBytes + 1];

        assertThrows(Exception.class, () -> {
            cipher.encipher(block, invalidKeyA);
        });
        assertThrows(Exception.class, () -> {
            cipher.encipher(block, invalidKeyB);
        });
        assertThrows(Exception.class, () -> {
            cipher.encipher(block, invalidKeyC);
        });
    }

    @Test
    public void testInvalidBlockSizes() {
        for (KeySize size : KeySize.values()) {
            invalidBlockTestHelper(size);
        }
    }

    @Test
    public void testInvalidKeySizes() {
        for (KeySize size : KeySize.values()) {
            invalidKeySizeHelper(size);
        }
    }
}
