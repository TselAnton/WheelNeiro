package com.tsel.neuro.perceptron;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.repository.ResultRepository;
import com.tsel.neuro.service.ResultService;
import com.tsel.neuro.utils.HandlerUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PerceptronTrainerCreateDataSetTest {

    private static final String TEST_DATA_FILE_NAME = "testDataFile.aff";

    @Mock
    private ResultService resultService;

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private PerceptronSettings settings;

    @Mock
    private Perceptron perceptron;

    @InjectMocks
    private PerceptronTrainer trainer;

    private List<Result> resultSet = asList(
            new Result(0, 1L), new Result(0, 2L), new Result(1, 3L),
            new Result(2, 4L), new Result(3, 5L), new Result(0, 6L),
            new Result(0, 7L), new Result(1, 8L), new Result(1, 9L),
            new Result(1, 10L), new Result(2, 11L), new Result(1, 12L),
            new Result(2, 13L), new Result(1, 14L), new Result(0, 15L),

            new Result(3, 2000L),  // Invalid data result

            new Result(0, 16L), new Result(2, 17L), new Result(1, 18L),
            new Result(1, 19L), new Result(0, 20L), new Result(0, 21L)    //21
    );

    @Before
    public void before() {
        when(settings.getInputsCount()).thenReturn(5);
        when(settings.getTestFilePath()).thenReturn(TEST_DATA_FILE_NAME);
        when(resultService.getResultSet(6)).thenReturn(resultSet.subList(0, 6));
        when(resultService.getResultSet(6, 2L)).thenReturn(resultSet.subList(1, 7));
        when(resultService.getResultSet(6, 3L)).thenReturn(resultSet.subList(2, 8));
        when(resultService.getResultSet(6, 4L)).thenReturn(resultSet.subList(3, 9));
        when(resultService.getResultSet(6, 5L)).thenReturn(resultSet.subList(4, 10));
        when(resultService.getResultSet(6, 6L)).thenReturn(resultSet.subList(5, 11));
        when(resultService.getResultSet(6, 7L)).thenReturn(resultSet.subList(6, 12));
        when(resultService.getResultSet(6, 8L)).thenReturn(resultSet.subList(7, 13));
        when(resultService.getResultSet(6, 9L)).thenReturn(resultSet.subList(8, 14));
    }

    @Test
    public void checkCreateFile() throws Exception {
        trainer.createDataFile();
        List<String> allLines = Files.readAllLines(Paths.get(TEST_DATA_FILE_NAME));

        assertEquals(allLines.get(0), "@RELATION testData");
        assertEquals(allLines.get(2), "@ATTRIBUTE x1 NUMERIC");
        assertEquals(allLines.get(3), "@ATTRIBUTE x2 NUMERIC");
        assertEquals(allLines.get(4), "@ATTRIBUTE x3 NUMERIC");
        assertEquals(allLines.get(5), "@ATTRIBUTE x4 NUMERIC");
        assertEquals(allLines.get(6), "@ATTRIBUTE x5 NUMERIC");
        assertEquals(allLines.get(7), "@ATTRIBUTE result NUMERIC");
        assertEquals(allLines.get(9), "@DATA");
        assertEquals(allLines.get(10), buildResultString(0, 0, 1, 2, 3, 0));
        assertEquals(allLines.get(11), buildResultString(0, 1, 2, 3, 0, 0));
        assertEquals(allLines.get(12), buildResultString(1, 2, 3, 0, 0, 1));
        assertEquals(allLines.get(13), buildResultString(2, 3, 0, 0, 1, 1));
        assertEquals(allLines.get(14), buildResultString(3, 0, 0, 1, 1, 1));
        assertEquals(allLines.get(15), buildResultString(0, 0, 1, 1, 1, 2));
        assertEquals(allLines.get(16), buildResultString(0, 1, 1, 1, 2, 1));
        assertEquals(allLines.get(17), buildResultString(1, 1, 1, 2, 1, 2));
        assertEquals(allLines.get(18), buildResultString(1, 1, 2, 1, 2, 1));
        assertEquals(allLines.get(19), buildResultString(1, 2, 1, 2, 1, 0));
        assertEquals(allLines.get(20), buildResultString(3, 0, 2, 1, 1, 0));
        assertEquals(allLines.get(21), buildResultString(0, 2, 1, 1, 0, 0));
    }

    //TODO: Остановился тут

    private String buildResultString(Integer... values) {
        return Stream.of(values)
                .map(HandlerUtils::getNormalizeNum)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
