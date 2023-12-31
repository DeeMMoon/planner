package com.example.planner.todo.controllers;

import com.example.planner.todo.search.PrioritySearchValues;
import com.example.planner.todo.service.PriorityService;
import com.example.planner.entity.entity.Priority;
import com.example.planner.utils.rest.resttemplate.UserRestBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityService priorityService;

    private UserRestBuilder userRestBuilder;

    public PriorityController(PriorityService priorityService, UserRestBuilder userRestBuilder) {
        this.priorityService = priorityService;
        this.userRestBuilder = userRestBuilder;
    }


    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody Long userId) {
        return priorityService.findAll(userId);
    }


    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {

        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userRestBuilder.userExists(priority.getUserId())) {
            return ResponseEntity.ok(priorityService.add(priority));
        }

        return new ResponseEntity("user id=" + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);

    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {

        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);


        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {

        Priority priority = null;

        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priority);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            priorityService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {

        if (prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: user id", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId()));
    }
}
