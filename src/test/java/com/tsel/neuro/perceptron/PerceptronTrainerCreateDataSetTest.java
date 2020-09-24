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
        when(resultService.getResultSet(6, 10L)).thenReturn(resultSet.subList(9, 15));
        when(resultService.getResultSet(6, 11L)).thenReturn(resultSet.subList(10, 16));
        when(resultService.getResultSet(6, 12L)).thenReturn(resultSet.subList(11, 17));
        when(resultService.getResultSet(6, 13L)).thenReturn(resultSet.subList(12, 18));
        when(resultService.getResultSet(6, 14L)).thenReturn(resultSet.subList(13, 19));
        when(resultService.getResultSet(6, 15L)).thenReturn(resultSet.subList(14, 20));
        when(resultService.getResultSet(6, 2000L)).thenReturn(resultSet.subList(15, 21));
        when(resultService.getResultSet(6, 16L)).thenReturn(resultSet.subList(16, 22));
        when(resultService.getResultSet(6, 17L)).thenReturn(resultSet.subList(17, 22));
        when(resultService.getResultSet(6, 18L)).thenReturn(resultSet.subList(18, 22));
        when(resultService.getResultSet(6, 19L)).thenReturn(resultSet.subList(19, 22));
        when(resultService.getResultSet(6, 20L)).thenReturn(resultSet.subList(20, 22));
        when(resultService.getResultSet(6, 21L)).thenReturn(resultSet.subList(21, 22));
    }

    @Test
    public void checkCreateFile() throws Exception {
        trainer.createDataFile();
        List<String> allLines = Files.readAllLines(Paths.get(TEST_DATA_FILE_NAME));

        assertEquals( "@RELATION testData", allLines.get(0));
        assertEquals("@ATTRIBUTE x1 NUMERIC", allLines.get(2));
        assertEquals("@ATTRIBUTE x2 NUMERIC", allLines.get(3));
        assertEquals("@ATTRIBUTE x3 NUMERIC", allLines.get(4));
        assertEquals("@ATTRIBUTE x4 NUMERIC", allLines.get(5));
        assertEquals("@ATTRIBUTE x5 NUMERIC", allLines.get(6));
        assertEquals("@ATTRIBUTE result NUMERIC", allLines.get(7));
        assertEquals("@DATA", allLines.get(9));
        assertEquals(buildResultString(0, 0, 1, 2, 3, 0), allLines.get(10));
        assertEquals(buildResultString(0, 1, 2, 3, 0, 0), allLines.get(11));
        assertEquals(buildResultString(1, 2, 3, 0, 0, 1), allLines.get(12));
        assertEquals(buildResultString(2, 3, 0, 0, 1, 1), allLines.get(13));
        assertEquals(buildResultString(3, 0, 0, 1, 1, 1), allLines.get(14));
        assertEquals(buildResultString(0, 0, 1, 1, 1, 2), allLines.get(15));
        assertEquals(buildResultString(0, 1, 1, 1, 2, 1), allLines.get(16));
        assertEquals(buildResultString(1, 1, 1, 2, 1, 2), allLines.get(17));
        assertEquals(buildResultString(1, 1, 2, 1, 2, 1), allLines.get(18));
        assertEquals(buildResultString(1, 2, 1, 2, 1, 0), allLines.get(19));
        assertEquals(buildResultString(0, 2, 1, 1, 0, 0), allLines.get(20));
    }

    private String buildResultString(Integer... values) {
        return Stream.of(values)
                .map(HandlerUtils::getNormalizeNum)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
