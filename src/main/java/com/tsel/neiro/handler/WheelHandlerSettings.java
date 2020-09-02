package com.tsel.neiro.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "handler")
public class WheelHandlerSettings {
    private String webSite;
    private Integer timeout;
    private Integer ticksToUpdate;
}
