package com.tsel.neuro.perceptron;

import static java.lang.Math.abs;
import static java.lang.String.format;
import static java.util.Collections.emptyList;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.repository.ResultRepository;
import com.tsel.neuro.service.ResultService;
import com.tsel.neuro.utils.HandlerUtils;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerceptronTrainer implements Runnable {

    private static final String RELATION = "@RELATION testData\n";
    private static final String DATA = "@DATA";
    private static final String ATTRIBUTE_PATTERN = "@ATTRIBUTE x%d NUMERIC";
    private static final String RESULT_ATTRIBUTE = "@ATTRIBUTE result NUMERIC";

    private final ResultService resultService;
    private final ResultRepository resultRepository;
    private final PerceptronSettings settings;
    private final Perceptron perceptron;

    public PerceptronTrainer(@Autowired ResultService resultService,
                             @Autowired ResultRepository resultRepository,
                             @Autowired PerceptronSettings settings,
                             @Autowired Perceptron perceptron) {
        this.resultService = resultService;
        this.resultRepository = resultRepository;
        this.settings = settings;
        this.perceptron = perceptron;
    }

    @Override
    public void run() {
        createDataFile();
        trainPerceptron();
    }

    protected void trainPerceptron() {

    }

    protected void createDataFile() {
        Result lastResult = null;
        List<Result> resultSet = resultService.getResultSet(settings.getInputsCount() + 1);
        if (!isValidDataSetSize(resultSet)) {
            log.warn("Not enough data for create data set file");
            Thread.currentThread().interrupt();
        }

        try (PrintWriter writer = new PrintWriter(Paths.get(settings.getTestFilePath()).toString(), "UTF-8")) {
            do {
                if (lastResult == null) {
                    printFileHeader(writer);
                }
                if (lastResult == null || isValidDataSet(resultSet)) {
                    writer.println(resultSet.stream()
                            .map(Result::getValue)
                            .map(HandlerUtils::getNormalizeNum)
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
                    );
                }

                lastResult = resultSet.get(1);
                resultSet = resultService.getResultSet(settings.getInputsCount() + 1, lastResult.getDate());
            } while (isValidDataSetSize(resultSet));

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            log.error("Exception while create data set file", e);
            Thread.currentThread().interrupt();
        }
    }

    private boolean isValidDataSetSize(List<Result> dataSet) {
        return dataSet.size() >= settings.getInputsCount() + 1;
    }

    private boolean isValidDataSet(List<Result> dataSet) {
        for (int i = 0; i < dataSet.size() - 1; i++) {
            if (abs(dataSet.get(i + 1).getDate() - dataSet.get(i).getDate()) > 1000) {
                return false;
            }
        }

        return true;
    }

    private void printFileHeader(PrintWriter writer) {
        writer.println(RELATION);

        for (int i = 0; i < settings.getInputsCount(); i++) {
            writer.println(format(ATTRIBUTE_PATTERN, i+1));
        }
        writer.println(RESULT_ATTRIBUTE + "\n");
        writer.println(DATA);
    }
}
