package com.bhowmikbikash.SpringBootPractice.service;

import com.bhowmikbikash.SpringBootPractice.entity.Employee;
import com.bhowmikbikash.SpringBootPractice.exception.EmployeeAlreadyExistException;
import com.bhowmikbikash.SpringBootPractice.exception.EmployeeNotFoundException;
import com.bhowmikbikash.SpringBootPractice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Employee savedEmployee =null;
        try {
            String email = employee.getEmail() == null ? "" : employee.getEmail().trim();

            if (email.isEmpty()) {
                throw new IllegalAccessException("Email id is not correct");
            }

            boolean isEmployeeExists = employeeRepository.existsByEmail(email);

            if (isEmployeeExists) {
                throw new EmployeeAlreadyExistException("Employee already exist with email : " + email);
            }

            savedEmployee = employeeRepository.save(employee);
            log.info("New employee saved {} : ", employee);

        } catch (Exception e) {
            throw new EmployeeAlreadyExistException("Employee already exists with id " + employee.getEmployeeId(), e);
        }


        return savedEmployee;
    }

    @Override
    public List<Employee> findAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        log.info("Total {} employees found.", allEmployees.size());

        if (allEmployees.isEmpty()) {
            throw new EmployeeNotFoundException("No employee found");
        }
        return allEmployees;
    }

    @Override
    public Employee findEmployeeById(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);

            if (employee.isPresent()) {
                return employee.get();
            } else {
                throw new EmployeeNotFoundException("Employee not found with Id : " + id);
            }
        } catch (EmployeeNotFoundException e) {
            throw new EmployeeNotFoundException(e);
        }

    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee savedEmployee = null;

        try {
            Employee employeeToUpdate = employeeRepository.findById(id)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

            String email = employee.getEmail() == null ? "" : employee.getEmail().trim();
            if(!email.equals(employeeToUpdate.getEmail())){
                throw new IllegalAccessException("Email id is not correct.");
            }

            employeeToUpdate.setCompany(employee.getCompany());
            employeeToUpdate.setSalaryPerMonth(employee.getSalaryPerMonth());
            employeeToUpdate.setDateOfJoining(employee.getDateOfJoining());
            employeeToUpdate.setCompany(employee.getCompany());

            savedEmployee = employeeRepository.save(employeeToUpdate);

            log.info("Employee found {} : ", savedEmployee);
        } catch (Exception e) {
            throw new EmployeeNotFoundException(e);
        }

        return savedEmployee;

    }

    @Override
    public void deleteEmployee(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Employee not found to delete with id + " + id, e);
        }
    }

    @Override
    public Employee patchEmployee(Long id, Employee employee) {
        try {
            Employee existing = employeeRepository.findById(id)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

            // Prevent changing the id if provided
            if (employee.getEmployeeId() != null && !employee.getEmployeeId().equals(id)) {
                throw new IllegalAccessException("employeeId cannot be changed");
            }

            // If email is provided, ensure it matches existing email (disallow changing email via patch)
            if (employee.getEmail() != null) {
                String providedEmail = employee.getEmail().trim();
                if (!providedEmail.equals(existing.getEmail())) {
                    throw new IllegalAccessException("Email id is not correct.");
                }
            }

            // Update only non-null fields
            if (employee.getEmployeeName() != null) {
                existing.setEmployeeName(employee.getEmployeeName());
            }
            if (employee.getCompany() != null) {
                existing.setCompany(employee.getCompany());
            }
            if (employee.getSalaryPerMonth() != null) {
                existing.setSalaryPerMonth(employee.getSalaryPerMonth());
            }
            if (employee.getDateOfJoining() != null) {
                existing.setDateOfJoining(employee.getDateOfJoining());
            }

            Employee saved = employeeRepository.save(existing);
            log.info("Employee patched {} : ", saved);
            return saved;
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new EmployeeNotFoundException(e);
        }
    }
}
