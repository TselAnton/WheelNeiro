package com.tsel.neuro.perceptron;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.repository.ResultRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tsel.neuro.utils.HandlerUtils.getColorName;

@Slf4j
@Component
public class PerceptronWorker {

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
    }

    /**
     * Get the percentage of successful predictions
     * @return percentage of successful predictions
     */
    public double getSuccessStat() {
        return 100.0 * ((double)successElements / (double)countOfElements);
    }

    /**
     * Get result.
     * If it is necessary to retrain the perceptron,
     * this creates a new stream to create a new data file and retrain the neural network
     * @param result Last result
     */
    public void handleEvent(Result result) {
        log.info("Current color: {}", getColorName(result.getValue()));
        countOfElements += 1;

        //TODO: Ещё подумать над этим моментом
//        if (result.equals(lastResult)) successElements++;
//        lastResult = result;

        if (isNeededRelearn()) {
            lastLearnUpdate = LocalDateTime.now();
            Thread updateThread = new Thread(perceptronTrainer);
            updateThread.setName("Perceptron Trainer ");
            updateThread.start();
        }
    }

    private boolean isNeededRelearn() {
        return (lastLearnUpdate == null && resultRepository.count() >= perceptronSettings.getInputsCount() * 3) ||
            (lastLearnUpdate != null && Duration.between(lastLearnUpdate, LocalDateTime.now())
                    .toHours() >= perceptronSettings.getHoursForUpdate());
    }
}
