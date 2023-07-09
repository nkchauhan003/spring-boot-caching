package com.cb.service;

public interface EmployeeService<T> {

    T create(T t);

    T retrieve(int id);

    T update(T t);

    void delete(int id);
}
