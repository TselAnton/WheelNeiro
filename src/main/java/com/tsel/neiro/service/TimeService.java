package com.tsel.neiro.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TimeService {

    public long getCurrentTimeInMilSec() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
