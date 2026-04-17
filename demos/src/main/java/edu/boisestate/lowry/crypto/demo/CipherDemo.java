package edu.boisestate.lowry.crypto.demo;

import edu.boisestate.lowry.crypto.*;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Demonstrates usage of RC6 in CTR mode using a randomly
 * generated key.
 *
 * @author Jayce Lowry
 */
public class CipherDemo {
    public static void main(String[] args) {
        // Create instances
        BlockCipher rc6 = new RC6Cipher(KeySize.BITS_128);
        SymmetricCipher cipher = new CTRMode(rc6);

        // Create random 16-byte (128-bit) key
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[KeySize.BITS_128.numBytes];
        random.nextBytes(key);

        // Encrypt/decrypt a string
        String message = "Imagine an imaginary menagerie manager managing an imaginary menagerie";
        byte[] ciphertext = cipher.encrypt(message.getBytes(), key);
        byte[] plaintext = cipher.decrypt(ciphertext, key);

        System.out.printf("Message: %s\n", message);
        System.out.printf("Ciphertext (interpreted as text): %s\n", new String(ciphertext, StandardCharsets.UTF_8));
        System.out.printf("Decrypted plaintext: %s\n", new String(plaintext, StandardCharsets.UTF_8));
    }
}