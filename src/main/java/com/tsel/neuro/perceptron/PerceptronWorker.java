package com.tsel.neuro.perceptron;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.repository.ResultRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerceptronWorker extends Thread {

    private final ResultRepository resultRepository;
    private final Perceptron perceptron;
    private final PerceptronTrainer perceptronTrainer;
    private final PerceptronDataCreator perceptronDataCreator;
    private final PerceptronSettings perceptronSettings;

    private LocalDateTime lastLearnUpdate;

    public PerceptronWorker(@Autowired ResultRepository resultRepository,
                            @Autowired Perceptron perceptron,
                            @Autowired PerceptronTrainer perceptronTrainer,
                            @Autowired PerceptronDataCreator perceptronDataCreator,
                            @Autowired PerceptronSettings perceptronSettings) {
        this.resultRepository = resultRepository;
        this.perceptron = perceptron;
        this.perceptronTrainer = perceptronTrainer;
        this.perceptronDataCreator = perceptronDataCreator;
        this.perceptronSettings = perceptronSettings;
        this.lastLearnUpdate = null;
        this.setName("Perceptron Worker");
    }

    @Override
    public void run() {

    }

    public void handleEvent(Result lastColor) {
        if (isNeededRelearn()) {
            Thread
        }
    }

    private boolean isNeededRelearn() {
        return (lastLearnUpdate == null &&
            ((int)(resultRepository.count() * perceptronSettings.getUpdatePercentage()) >=
                perceptronSettings.getInputsCount())) ||
            (lastLearnUpdate != null &&
                Duration.between(lastLearnUpdate, LocalDateTime.now())
                    .toHours() >= perceptronSettings.getHoursForUpdate());
    }
}
