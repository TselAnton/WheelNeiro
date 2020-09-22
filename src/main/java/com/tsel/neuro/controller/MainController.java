package com.tsel.neuro.controller;

import javax.ws.rs.GET;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    //TODO: Настроить вывод view

    @GET
    public String getMainPage() {
        return "Main page";
    }
}
