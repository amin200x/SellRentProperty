package com.amin.sellrenthouse.controller;

import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.manager.UserManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserController {
    private static UserController instance = new UserController();

    private UserController() {

    }

    public static UserController getInstance() {
        return instance;
    }

    public Exception signUp(User user) throws ExecutionException, InterruptedException {
       return UserManager.getInstance().signUp(user);
    }

    public List<User> signIn(User user) throws ExecutionException, InterruptedException {
       return  UserManager.getInstance().signIn(user);
    }
    public List<User> getUser(User user) throws ExecutionException, InterruptedException {
        return  UserManager.getInstance().getUser(user);
    }

    public Exception removeUser(User user) throws ExecutionException, InterruptedException {
      return UserManager.getInstance().removeUser(user);
    }

    public Exception updateProfile(User user) throws ExecutionException, InterruptedException {
       return UserManager.getInstance().updateProfile(user);
    }
}