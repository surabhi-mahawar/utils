package com.uci.utils.encryption;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.uci.utils.encryption.AESWrapper.encodeKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Only created to describe how to use this.
@Slf4j
@ExtendWith(MockitoExtension.class)
class AESWrapperTest {

    /*
     * Secret Key must be in the form of 16 byte like,
     *
     * private static final byte[] secretKey = new byte[] { ‘m’, ‘u’, ‘s’, ‘t’, ‘b’,
     * ‘e’, ‘1’, ‘6’, ‘b’, ‘y’, ‘t’,’e’, ‘s’, ‘k’, ‘e’, ‘y’};
     *
     * below is the direct 16byte string we can use
     */
    public static final String secretKeyAsAString = "____16LENGTH____";
    public static final String toEncrypt = "phone:9415787824";

    @SneakyThrows
    @BeforeEach
    public void init() {
    }

    @Test
    public void stringEncryptionTest() throws Exception {

        String encodedBase64Key = encodeKey(secretKeyAsAString);
        String encryptedString = AESWrapper.encrypt(toEncrypt, encodedBase64Key);
        String decryptedString = AESWrapper.decrypt(encryptedString, encodedBase64Key);
        assertEquals(decryptedString, toEncrypt);

    }

}