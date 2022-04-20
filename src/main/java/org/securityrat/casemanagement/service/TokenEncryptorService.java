package org.securityrat.casemanagement.service;

import org.securityrat.casemanagement.security.AesEncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.BouncyCastleAesCbcBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

@Service
public class TokenEncryptorService {

    private final AesEncryptionProperties aesEncryptionProperties;

    private final BytesEncryptor encryptor;

    @Autowired
    public TokenEncryptorService(AesEncryptionProperties aesEncryptionProperties) {
        this.aesEncryptionProperties = aesEncryptionProperties;
        this.encryptor = new BouncyCastleAesCbcBytesEncryptor(aesEncryptionProperties.getSecretKey(), aesEncryptionProperties.getSalt(), aesEncryptionProperties.getIvGenerator());
    }

    public byte[] encrypt(String value) {
        return this.encryptor.encrypt(value.getBytes());
    }

    public byte[] decrypt(String encryptedValue, String salt) {
        BytesEncryptor decryptor = new BouncyCastleAesCbcBytesEncryptor(this.aesEncryptionProperties.getSecretKey(), salt, this.aesEncryptionProperties.getIvGenerator());
        return decryptor.decrypt(encryptedValue.getBytes());
    }


    public String generateSalt() {
        return this.aesEncryptionProperties.getSalt();
    }
}
