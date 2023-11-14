package com.example.userService.controller;

import com.example.userService.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    public ResponseEntity getProductById() {
        ResponseEntity response = new ResponseEntity(departmentService.findAllDepartment(), HttpStatus.valueOf(200));
        return response;

    }
}
