package com.andhikayusup.moviex.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
public class JwtConfig {
    
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
}
