package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;
/**
 * Any class that handles operations on entities
 */
public interface ICrudService<T> {

    List<T> getAll();

    T save(T entity);

    Optional<T> getById(int id);

    T update(int id, T entity);

    void delete(int id);
}
