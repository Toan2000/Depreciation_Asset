package com.example.userService.service;

import com.example.userService.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface UserService {
    List<User> findAllUser(Pageable pageable);
    User findUserById(Long id);
}
