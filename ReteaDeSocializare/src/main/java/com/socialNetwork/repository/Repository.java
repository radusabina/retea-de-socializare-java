package com.socialNetwork.repository;

import com.socialNetwork.domain.Entity;
import com.socialNetwork.exceptions.RepositoryException;

public interface Repository<ID, T extends Entity<ID>> {
    void save(T obj) throws RepositoryException;
    void read(T obj) throws RepositoryException;
    void update(ID id, T obj) throws RepositoryException;
    T delete(ID id) throws RepositoryException;
    T findAfterId(ID id) throws RepositoryException;
    Iterable<T> getAll();
    int size();
}
