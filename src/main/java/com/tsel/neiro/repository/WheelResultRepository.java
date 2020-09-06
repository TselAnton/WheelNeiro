package com.tsel.neiro.repository;

import com.tsel.neiro.data.WheelResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WheelResultRepository extends CrudRepository<WheelResult, Long> {
}
