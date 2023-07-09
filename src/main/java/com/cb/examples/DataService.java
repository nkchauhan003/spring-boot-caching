package com.cb.examples;

import com.cb.model.Employee;
import com.cb.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Cacheable("employee-cache")
    public Optional<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    @CacheEvict("employee-cache")
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @CacheEvict(value = "employee-cache", allEntries = true)
    public void deleteAll(int id) {
        employeeRepository.deleteById(id);
    }

    @CachePut(key = "#employee.getId", cacheNames = {"employee-cache"})
    public Employee update(Employee employee) {
        Employee employeeDb = employeeRepository.findById(employee.getId()).orElseThrow(
                () -> new EntityNotFoundException("Employee with id: " + employee.getId() + ", not available."));
        employeeDb.setName(employee.getName());
        employeeDb.setDesignation(employee.getDesignation());
        return employeeRepository.save(employeeDb);
    }

    @Caching(evict = {
            @CacheEvict("address-cache"),
            @CacheEvict(value = "employee-cache", key = "#employee.id")
    })
    public Optional<Employee> getEmployee(Employee employee) {
        return employeeRepository.findById(employee.getId());
    }
}
