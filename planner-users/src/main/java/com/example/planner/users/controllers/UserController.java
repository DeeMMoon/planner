package com.example.planner.users.controllers;

import com.example.planner.users.search.UserSearchValues;
import com.example.planner.users.services.UserService;
import com.example.planner.entity.entity.User;
import com.example.planner.utils.rest.webclient.UserWebClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Optional;
@RestController
@RequestMapping("/user")
public class UserController {

    public static final String ID_COLUMN = "id";
    private final UserService userService;
    private final UserWebClientBuilder userWebClientBuilder;
    private MessageFuncActions messageFuncActions;

    @Autowired
    public UserController(MessageFuncActions messageFuncActions, UserService userService, UserWebClientBuilder userWebClientBuilder) {
        this.userService = userService;
        this.userWebClientBuilder = userWebClientBuilder;
        this.messageFuncActions = messageFuncActions;
    }


    // добавление
    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {
        if (user.getId() != null && user.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        user = userService.add(user);

//        if (user != null) {
//            // заполняем начальные данные пользователя (в параллелном потоке)
//            userWebClientBuilder.initUserData(user.getId()).subscribe(result -> {
//                        System.out.println("user populated: " + result);
//                    }
//            );
//        }

//        if (user != null) { // если пользователь добавился
//            messageProducer.initUserData(user.getId()); // отправляем сообщение в канал
//        }

        if (user != null) {
            messageFuncActions.sendNewUserMessage(user.getId());
        }

        return ResponseEntity.ok(user);

    }
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {

        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        userService.update(user);

        return new ResponseEntity(HttpStatus.OK);

    }
    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody Long userId) {
        try {
            userService.deleteByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
        try {
            userService.deleteByUserEmail(email);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long id) {

        Optional<User> userOptional = userService.findById(id);

        try {
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);

    }
    @PostMapping("/email")
    public ResponseEntity<User> findByEmail(@RequestBody String email) {

        User user;
        try {
            user = userService.findByEmail(email);
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(user);
    }
    @PostMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) throws ParseException {

        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;

        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

//        // проверка на обязательные параметры - если они нужны по задаче
//        if (email == null || email.trim().length() == 0) {
//            return new ResponseEntity("missed param: user email", HttpStatus.NOT_ACCEPTABLE);
//        }

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : null;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> result = userService.findByParams(email, username, pageRequest);

        return ResponseEntity.ok(result);

    }


}
