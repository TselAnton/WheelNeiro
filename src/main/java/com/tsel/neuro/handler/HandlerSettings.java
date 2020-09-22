package com.tsel.neuro.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("singleton")
@ConfigurationProperties(prefix = "handler")
public class HandlerSettings {
    private String webSite;
    private Integer pointCount;
    private String connectorName;
}
