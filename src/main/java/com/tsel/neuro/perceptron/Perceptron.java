package com.tsel.neuro.perceptron;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weka.classifiers.functions.MultilayerPerceptron;

@Getter
@Component
public class Perceptron {

    private final MultilayerPerceptron mlp;

    public Perceptron(@Autowired PerceptronSettings settings) {
        mlp = new MultilayerPerceptron();
        mlp.setMomentum(settings.getMoment());
        mlp.setLearningRate(settings.getLearningRate());
        mlp.setValidationThreshold(settings.getEpochNumber());
        mlp.setHiddenLayers(settings.getHiddenLayers());
    }
}
