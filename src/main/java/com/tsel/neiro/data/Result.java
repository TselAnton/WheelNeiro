package com.tsel.neiro.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wheel_result")
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long date;
    private Integer value;

//    public Result(Integer value) {
//        this.value = value;
//        this.date = LocalDateTime.now()
//                .atZone(ZoneId.systemDefault())
//                .toInstant()
//                .toEpochMilli();
//    }
}
