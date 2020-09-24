package com.tsel.neuro.perceptron;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.repository.ResultRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerceptronWorker extends Thread {

    private final ResultRepository resultRepository;
    private final Perceptron perceptron;
    private final PerceptronTrainer perceptronTrainer;
    private final PerceptronSettings perceptronSettings;

    private LocalDateTime lastLearnUpdate;

    private long countOfElements;
    private long successElements;
    private Result lastResult;

    public PerceptronWorker(@Autowired ResultRepository resultRepository,
                            @Autowired Perceptron perceptron,
                            @Autowired PerceptronTrainer perceptronTrainer,
                            @Autowired PerceptronSettings perceptronSettings) {
        this.resultRepository = resultRepository;
        this.perceptron = perceptron;
        this.perceptronTrainer = perceptronTrainer;
        this.perceptronSettings = perceptronSettings;
        this.lastLearnUpdate = null;
        this.setName("Perceptron Worker");
    }

    @Override
    public void run() {

    }

    public void handleEvent(Result lastColor) {
        if (isNeededRelearn()) {
            Thread updateThread = new Thread(perceptronTrainer);
            updateThread.setName("Perceptron Trainer");
            updateThread.start();

            try {
                updateThread.join();
            } catch (InterruptedException e) {
                log.error("Something went wrong while train perceptron", e);
            }
        }
    }

    private boolean isNeededRelearn() {
        return (lastLearnUpdate == null && resultRepository.count() >= perceptronSettings.getInputsCount() * 3) ||
            (lastLearnUpdate != null && Duration.between(lastLearnUpdate, LocalDateTime.now())
                    .toHours() >= perceptronSettings.getHoursForUpdate());
    }
}
