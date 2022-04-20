package org.securityrat.casemanagement.security;

import lombok.Getter;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class AesEncryptionProperties {

    @Getter
    private final String secretKey;

    @Getter
    private final String salt;

    @Getter
    private final BytesKeyGenerator ivGenerator;

    public AesEncryptionProperties(String secretKey, String salt) {
        this.secretKey = secretKey;
        this.salt = salt;
        this.ivGenerator = KeyGenerators.secureRandom(16);
    }
}
