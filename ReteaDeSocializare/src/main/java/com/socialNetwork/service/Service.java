package com.socialNetwork.service;

import com.socialNetwork.domain.Friendship;
import com.socialNetwork.domain.User;
import com.socialNetwork.exceptions.NetworkException;
import com.socialNetwork.exceptions.RepositoryException;
import com.socialNetwork.exceptions.ValidationException;

import java.util.List;

public interface Service {
    void add(String firstName, String lastName, String email, String password) throws ValidationException, RepositoryException;
    void updateUser(Long id, String firstName, String lastName, String email, String password) throws RepositoryException, ValidationException;
    User remove(Long id) throws RepositoryException, NetworkException;
    User getUser(Long id) throws RepositoryException;
    Friendship getFriendship(Long id) throws RepositoryException;
    User findUserAfterEmail(String email) throws RepositoryException;
    List<Friendship> findUserFriends(Long id);
    List<Friendship> findUserRequests(Long id);
    void makeFriends(Long id1, Long id2) throws NetworkException, ValidationException, RepositoryException;
    void updateFriends(Long friendshipId, Long idUser1, Long idUser2) throws ValidationException, RepositoryException, NetworkException;
    void removeFriends(Long id) throws NetworkException, ValidationException, RepositoryException;
    int numberOfCommunities();
    List<User> mostPopulatedCommunity();
    Iterable<User> getAllUsers();
    Iterable<Friendship> getAllFriendships();
    int numberOfUsers();
    int numberOfFriendships();
}
