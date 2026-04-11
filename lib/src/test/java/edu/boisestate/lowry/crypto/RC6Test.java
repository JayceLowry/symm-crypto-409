package edu.boisestate.lowry.crypto;

import org.junit.jupiter.api.DynamicTest;
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
}
