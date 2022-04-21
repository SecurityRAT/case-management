package org.securityrat.casemanagement.config;

import org.securityrat.casemanagement.domain.enumeration.TicketSystem;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

@Configuration
public class TicketSystemConfiguration {

    public TicketSystemConfiguration(ApplicationProperties applicationProperties) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (TicketSystem.valueOf(applicationProperties.getTicketSystem().getType()).equals(TicketSystem.JIRASERVER)) {
            checkJiraServerPrivateKey(applicationProperties.getJiraServer().getPrivateKey());
        }

    }

    private void checkJiraServerPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateBytes = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        kf.generatePrivate(keySpec);
    }
}
