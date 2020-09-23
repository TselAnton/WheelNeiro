package com.tsel.neuro.perceptron;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;

@Component
@Scope("singleton")
public class Perceptron {

    private final MultilayerPerceptron mlp;
    private Evaluation evaluation;

    public Perceptron(@Autowired PerceptronSettings settings) {
        mlp = new MultilayerPerceptron();
        mlp.setMomentum(settings.getMoment());
        mlp.setLearningRate(settings.getLearningRate());
        mlp.setValidationThreshold(settings.getEpochNumber());
        mlp.setHiddenLayers(settings.getHiddenLayers());
    }

    public void trainMLP() {

    }

    public void predict() {

    }
}
