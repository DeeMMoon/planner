package com.example.planner.todo.controllers;

import com.example.planner.todo.service.StatService;
import com.example.planner.entity.entity.Stat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody Long userId) {
        return ResponseEntity.ok(statService.findStat(userId));
    }


}
