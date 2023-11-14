package com.example.userService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {
    @GetMapping("/home")
    public String home(){
        return "This is Home Page";
    }

}
