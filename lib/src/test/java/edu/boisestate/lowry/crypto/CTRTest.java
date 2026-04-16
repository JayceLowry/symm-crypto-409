package edu.boisestate.lowry.crypto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;

/**
 * Unit tests for CTRMode.
 *
 * @author Jayce Lowry
 */
public class CTRTest {
    @Test
    public void testIncrementNull() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        assertThrows(IllegalArgumentException.class, () -> {
            cipher.incrementBlock(null, 0, 0);
        });
    }

    @Test
    public void testIncrementOutOfBounds() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        byte[] block = new byte[Long.BYTES];
        assertThrows(IllegalArgumentException.class, () -> {
            cipher.incrementBlock(block, -1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cipher.incrementBlock(block, 0, 20);
        });
    }

    @Test
    public void testIncrementNothing() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        byte[] blockBefore = new byte[Long.BYTES];
        byte[] blockAfter = new byte[Long.BYTES];
        cipher.incrementBlock(blockAfter, 0, 0);

        assertArrayEquals(blockBefore, blockAfter);
    }

    @Test
    public void testIncrementLongBlock() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        byte[] block = new byte[Long.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(block);

        cipher.incrementBlock(block, 0, block.length);
        assertEquals(1, buffer.getLong());
    }

    @Test
    public void testIncrementLongBlockBulk() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        byte[] block = new byte[Long.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(block);
        buffer.mark();

        for (int i = 0; i < 100; i++) {
            assertEquals(i, buffer.getLong());
            cipher.incrementBlock(block, 0, block.length);
            buffer.reset();
        }
    }

    @Test
    public void testIncrementOverflow() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        long value = -1;
        buffer.putLong(value);
        byte[] block = buffer.array();

        assertThrows(IllegalStateException.class, () -> {
            cipher.incrementBlock(block, 0, block.length);
        });
    }

    @Test
    public void testIncrementFirstPosition() {
        CTRMode cipher = new CTRMode(new RC6Cipher(KeySize.BITS_128));
        byte[] block = new byte[Long.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(block);

        cipher.incrementBlock(block, 0, 1);
        assertEquals(72057594037927936L, buffer.getLong());
    }

    /**
     * Helper for testing encryption and decryption.
     *
     * @param size The key size.
     * @param key The key.
     */
    private void encryptDecryptHelper(KeySize size, byte[] key) {
        CTRMode cipher = new CTRMode(new RC6Cipher(size));
        String plaintext = "Imagine an imaginary menagerie manager managing an imaginary menagerie";

        byte[] ciphertext = cipher.encrypt(plaintext.getBytes(), key);
        byte[] decrypted = cipher.decrypt(ciphertext, key);

        assertArrayEquals(plaintext.getBytes(), decrypted);
        assertEquals(plaintext, new String(decrypted, Charset.defaultCharset()));
    }

    @Test
    public void testZeroKeyEncryptDecrypt128() {
        KeySize size = KeySize.BITS_128;
        byte[] key = new byte[size.numBytes];
        encryptDecryptHelper(size, key);
    }

    @Test
    public void testZeroKeyEncryptDecrypt192() {
        KeySize size = KeySize.BITS_192;
        byte[] key = new byte[size.numBytes];
        encryptDecryptHelper(size, key);
    }

    @Test
    public void testZeroKeyEncryptDecrypt256() {
        KeySize size = KeySize.BITS_256;
        byte[] key = new byte[size.numBytes];
        encryptDecryptHelper(size, key);
    }

    @Test
    public void testRandomKeyEncryptDecrypt128() {
        KeySize size = KeySize.BITS_128;
        SecureRandom rand = new SecureRandom();
        byte[] key = new byte[size.numBytes];
        rand.nextBytes(key);
        encryptDecryptHelper(size, key);
    }

    @Test
    public void testRandomKeyEncryptDecrypt192() {
        KeySize size = KeySize.BITS_192;
        SecureRandom rand = new SecureRandom();
        byte[] key = new byte[size.numBytes];
        rand.nextBytes(key);
        encryptDecryptHelper(size, key);
    }

    @Test
    public void testRandomKeyEncryptDecrypt256() {
        KeySize size = KeySize.BITS_256;
        SecureRandom rand = new SecureRandom();
        byte[] key = new byte[size.numBytes];
        rand.nextBytes(key);
        encryptDecryptHelper(size, key);
    }
}
