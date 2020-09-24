package com.tsel.neuro.perceptron;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weka.classifiers.functions.MultilayerPerceptron;

import javax.annotation.PreDestroy;
import java.io.*;

@Slf4j
@Getter
@Component
public class Perceptron implements Serializable {

    private static final long serialVersionUID = 9158043708768875337L;
    private static final String CLASS_PATH = "perceptron.out";

    private final MultilayerPerceptron mlp;

    public Perceptron(@Autowired PerceptronSettings settings) {
        Perceptron perceptron = getSerializedPerceptron();
        if (perceptron != null) {
            this.mlp = perceptron.mlp;
        } else {
            mlp = new MultilayerPerceptron();
            mlp.setMomentum(settings.getMoment());
            mlp.setLearningRate(settings.getLearningRate());
            mlp.setValidationThreshold(settings.getEpochNumber());
            mlp.setHiddenLayers(settings.getHiddenLayers());
        }
    }

    @PreDestroy
    private void serializePerceptron() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(CLASS_PATH))) {
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
        } catch (IOException e) {
            log.error("Can't save perceptron to file!", e);
        }
    }

    private Perceptron getSerializedPerceptron() {
        Perceptron perceptron = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(CLASS_PATH))) {
            perceptron = (Perceptron) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.warn("Can't get previous model ({})", e.getMessage());
        }
        return perceptron;
    }

}
