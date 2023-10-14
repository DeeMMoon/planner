package com.example.planner.todo.service;

import com.example.planner.todo.repositories.StatRepository;
import com.example.planner.entity.entity.Stat;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StatService {

    private final StatRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    public StatService(StatRepository repository) {
        this.repository = repository;
    }

    public Stat findStat(Long userId) {
        return repository.findByUserId(userId);
    }

}
