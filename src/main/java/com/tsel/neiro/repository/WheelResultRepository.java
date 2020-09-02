package com.tsel.neiro.repository;

import com.tsel.neiro.data.WheelResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WheelResultRepository extends JpaRepository<WheelResult, Long> {
}
