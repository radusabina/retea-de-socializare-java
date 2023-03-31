package com.socialNetwork.repository;

import com.socialNetwork.domain.Entity;
import com.socialNetwork.exceptions.RepositoryException;
import com.socialNetwork.repository.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDBRepository<ID, T extends Entity<ID>> implements Repository<ID, T> {

    private final Map<ID, T> entities;
    private final String url;
    private final String username;
    private final String password;
    private String sqlCommand;

    public AbstractDBRepository(String url, String username, String password, String sqlCommand) {
        entities = new HashMap<>();
        this.url = url;
        this.username = username;
        this.password = password;
        this.sqlCommand = sqlCommand;
        loadData();
    }

    public String getSqlCommand() { return sqlCommand;}
    public void setSqlCommand(String sqlCommand) { this.sqlCommand = sqlCommand;}

    public void loadData() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sqlCommand);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                T entity = extractEntity(resultSet);
                read(entity);
            }
        } catch (SQLException | RepositoryException e) {
            e.printStackTrace();
        }
    }

    protected abstract T extractEntity(ResultSet resultSet) throws SQLException;
    protected abstract PreparedStatement createInsertStatement(Connection connection, T entity) throws SQLException;
    protected abstract PreparedStatement createUpdateStatement(Connection connection, T entity) throws SQLException;
    protected abstract PreparedStatement createDeleteStatement(Connection connection, T entity) throws SQLException;

    protected PreparedStatement createStatementFromEntity(Connection connection, T entity) throws SQLException {
        char command = sqlCommand.charAt(0);
        return switch (command) {
            case 'I' -> createInsertStatement(connection, entity);
            case 'U' -> createUpdateStatement(connection, entity);
            case 'D' -> createDeleteStatement(connection, entity);
            default -> null;
        };
    }

    private void executeCommand(T entity) throws RepositoryException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = createStatementFromEntity(connection, entity)
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void save(T obj) throws RepositoryException {
        read(obj);
        executeCommand(obj);
    }

    public void read(T obj) throws RepositoryException {
        if (obj == null)
            throw new IllegalArgumentException("Entity cannot be null. \n");
        if (entities.containsKey(obj.getId()))
            throw new RepositoryException("Entity already exists. \n");
        if (entities.containsValue(obj))
            throw new RepositoryException("This element is already added. \n");
        entities.put(obj.getId(), obj);
    }

    @Override
    public void update(ID id, T obj) throws RepositoryException {
        if (obj == null)
            throw new IllegalArgumentException("Entity cannot be null. \n");
        if (!entities.containsKey(id))
            throw new RepositoryException("Element with this id does not exist. \n");
        entities.remove(id);
        entities.put(obj.getId(), obj);
        executeCommand(obj);
    }

    @Override
    public T delete(ID id) throws RepositoryException {
        if (!entities.containsKey(id))
            throw new RepositoryException("Element with this id does not exist. \n");
        T deleted =  entities.remove(id);
        executeCommand(deleted);
        return deleted;
    }

    @Override
    public T findAfterId(ID id) throws RepositoryException {
        if (!entities.containsKey(id))
            throw new RepositoryException("Element with this id does not exist. \n");
        return entities.get(id);
    }

    @Override
    public Iterable<T> getAll() { return entities.values();}

    @Override
    public int size() { return entities.size();}
}
