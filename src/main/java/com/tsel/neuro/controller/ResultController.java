package com.tsel.neuro.controller;

import com.tsel.neuro.data.Result;
import com.tsel.neuro.exception.NotFoundEntityException;
import com.tsel.neuro.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;

    public ResultController(@Autowired ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/last")
    public Result getLastResult() throws NotFoundEntityException {
        return resultService.getLastResult()
            .orElseThrow(() -> new NotFoundEntityException("Results is empty"));
    }
}
