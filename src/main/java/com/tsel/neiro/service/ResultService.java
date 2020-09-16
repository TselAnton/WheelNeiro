package com.tsel.neiro.service;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import com.tsel.neiro.data.Result;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class ResultService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Result> getResultSet(int limit, long lastDate) {
        return entityManager.createQuery(format("SELECT r FROM Result r WHERE r.date >= %d ORDER BY r.date", lastDate),
            Result.class).setMaxResults(limit).getResultList();
    }

    public List<Result> getResultSet(int limit) {
        return entityManager.createQuery("SELECT r FROM Result r ORDER BY r.date", Result.class)
            .setMaxResults(limit).getResultList();
    }

    public Optional<Result> getLastResult() {
        return ofNullable(entityManager.createQuery("SELECT r FROM Result r ORDER BY r.date DESC", Result.class)
            .setMaxResults(1)
            .getResultList())
            .orElse(emptyList())
            .stream()
            .findFirst();
    }
}
