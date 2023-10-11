package com.example.planner.todo.controllers;

import com.example.planner.todo.service.StatService;
import com.example.plannerentity.entity.Stat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// базовый URI не нужен, т.к. метод только один
public class StatController {

    private final StatService statService; // сервис для доступа к данным (напрямую к репозиториям не обращаемся)

    // используем автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public StatController(StatService statService) {
        this.statService = statService;
    }


    // для статистика всгда получаем только одну строку с id=1 (согласно таблице БД)
    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody Long userId) {

        // можно не использовать ResponseEntity, а просто вернуть коллекцию, код все равно будет 200 ОК
        return ResponseEntity.ok(statService.findStat(userId));
    }


}
