package com.cb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.cb.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cb.service.EmployeeService;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService<Employee> employeeCrudService;

    @PostMapping(value = "/", consumes = "application/json")
    public Employee create(@RequestBody Employee employee) {
        return employeeCrudService.create(employee);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Employee retrieve(@PathVariable int id) {
        log.debug("Retrieve employee start, id: " + id);
        var employee = employeeCrudService.retrieve(id);
        log.debug("Retrieve employee end, id: " + id);
        return employee;
    }

    @PutMapping(value = "/", consumes = "application/json")
    public Employee update(@RequestBody Employee employee) {
        log.debug("Update employee start, id: " + employee.getId());
        var employeeDb = employeeCrudService.update(employee);
        log.debug("Update employee end, id: " + employee.getId());
        return employeeDb;

    }

    @DeleteMapping(value = "/")
    public String delete(int id) {
        log.debug("Delete employee start, id: " + id);
        employeeCrudService.delete(id);
        log.debug("Delete employee end, id: " + id);
        return "Done";
    }
}
