package com.example.userService.repository;

import com.example.userService.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findAll();
}
