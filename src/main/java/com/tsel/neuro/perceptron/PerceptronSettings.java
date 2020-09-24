package com.tsel.neuro.perceptron;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties(prefix = "perceptron")
public class PerceptronSettings {

    private Double learningRate;
    private Double moment;
    private Integer epochNumber;
    private String hiddenLayers;
    private Integer inputsCount;
    private String testFilePath;
    private Integer hoursForUpdate;
}
