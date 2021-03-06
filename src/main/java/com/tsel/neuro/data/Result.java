package com.tsel.neuro.data;

import static com.tsel.neuro.utils.TimeUtils.getCurrentTimeInLong;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long date;
    private Integer value;

    public Result(Integer value) {
        this.date = getCurrentTimeInLong();
        this.value = value;
    }

    public Result(Integer value, Long date) {
        this.date = date;
        this.value = value;
    }
}
