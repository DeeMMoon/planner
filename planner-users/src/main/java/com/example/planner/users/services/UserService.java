package com.example.planner.users.services;

import com.example.planner.users.repositories.UserRepository;
import com.example.planner.entity.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service

@Transactional
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public User findByEmail(String email) {
        return repository.findFirstByEmail(email);
    }
    public User add(User user) {
        return repository.save(user);
    }

    public User update(User user) {
        return repository.save(user);
    }
    public void deleteByUserId(Long id) {
        repository.deleteById(id);
    }
    public void deleteByUserEmail(String email) {
        repository.deleteByEmail(email);
    }
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }
    public Page<User> findByParams(String username, String password, PageRequest paging) {
        return repository.findByParams(username, password, paging);
    }

}
