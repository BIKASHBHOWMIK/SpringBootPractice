package com.bhowmikbikash.SpringBootPractice.service;


import com.bhowmikbikash.SpringBootPractice.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> findAllEmployees();

    Employee findEmployeeById(Long id);

    Employee updateEmployee(Long id,Employee employee);

    void deleteEmployee(Long id);

    // Partial update (PATCH) - updates only provided/non-null fields except email and employeeId
    Employee patchEmployee(Long id, Employee employee);
}
