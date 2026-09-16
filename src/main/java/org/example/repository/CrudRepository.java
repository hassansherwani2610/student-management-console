package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    long count();
    T update(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}
