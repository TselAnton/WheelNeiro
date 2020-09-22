package com.tsel.neuro.perceptron;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import weka.classifiers.functions.MultilayerPerceptron;

@Component
@Scope("singleton")
public class Perceptron {

    private MultilayerPerceptron multilayerPerceptron;

    public Perceptron(@Autowired PerceptronSettings settings) {
        multilayerPerceptron = new MultilayerPerceptron();
        multilayerPerceptron.setMomentum(settings.getMoment());
        multilayerPerceptron.setLearningRate(settings.getLearningRate());
        multilayerPerceptron.setHiddenLayers(format("%s?", settings.getHiddenLayers()));
    }
}
