package com.tsel.neuro.controller;

import com.tsel.neuro.perceptron.PerceptronTrainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private PerceptronTrainer trainer;

    //TODO: Настроить вывод view

    @GET
    public String getMainPage() {
        return "Main page";
    }

    @GetMapping("/create")
    public void createDataSet() {
        trainer.createDataFile();
    }
}
