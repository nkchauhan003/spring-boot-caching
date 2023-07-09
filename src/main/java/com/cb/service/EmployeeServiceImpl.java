package com.cb.service;

import com.cb.model.Employee;
import com.cb.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService<Employee> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Cacheable("employee-cache")
    @Override
    public Employee retrieve(int id) {
        log.debug("Employee from db start, id: " + id);
        var employee = employeeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Employee with id: " + id + ", not available."));
        log.debug("Employee from db end, id: " + id);
        return employee;
    }

    @CachePut(key = "#employee.getId", cacheNames = {"employee-cache"})
    @Override
    public Employee update(Employee employee) {
        log.debug("Update from db start, id: " + employee.getId());
        Employee employeeDb = employeeRepository.findById(employee.getId()).orElseThrow(
                () -> new EntityNotFoundException("Employee with id: " + employee.getId() + ", not available."));

        employeeDb.setName(employee.getName());
        employeeDb.setDesignation(employee.getDesignation());
        log.debug("Update from db end, id: " + employee.getId());
        return employeeRepository.save(employeeDb);
    }

    @CacheEvict("employee-cache")
    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }
}
