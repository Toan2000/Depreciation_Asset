package com.example.userService.service;

import com.example.userService.model.Department;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface DepartmentService {
    public List<Department> findAllDepartment();
}
