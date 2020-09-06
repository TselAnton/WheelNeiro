package com.tsel.neiro;

import com.tsel.neiro.handler.ColorHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties
public class WheelNeiroApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WheelNeiroApplication.class, args);
        ColorHandler handler = context.getBean(ColorHandler.class);

        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
        handler.handle();
    }
}
