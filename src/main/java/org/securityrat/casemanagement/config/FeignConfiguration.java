package org.securityrat.casemanagement.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.securityrat.casemanagement")
public class FeignConfiguration {

}
