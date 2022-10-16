package org.securityrat.casemanagement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.securityrat.casemanagement.CaseManagementApp;
import org.securityrat.casemanagement.config.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CaseManagementApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
