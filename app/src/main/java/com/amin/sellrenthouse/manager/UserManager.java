package com.amin.sellrenthouse.manager;

import com.amin.sellrenthouse.dao.UserDao;
import com.amin.sellrenthouse.entities.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserManager {
    private static UserManager instance = new UserManager();
    private UserDao dao = new UserDao();

    private  UserManager(){

    }

    public static UserManager getInstance(){
        return instance;
    }
    public User createUser(String firstName, String lastName, String contactNumber,
                           String userName, String password){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setContactNumber(contactNumber);
        user.setUserName(userName);
        user.setPassword(password);
        user.setId(0);

        return user;
    }

    public Exception  signUp(User user) throws ExecutionException, InterruptedException {
        return dao.signUp(user);
    }

    public List<User> signIn(User user) throws ExecutionException, InterruptedException {
         return dao.signIn(user);
    }

    public List<User> getUser(User user) throws ExecutionException, InterruptedException {
        return  dao.getUser(user);
    }
    public Exception removeUser(User user) throws ExecutionException, InterruptedException {
       return dao.removeUser(user);
    }

    public Exception updateProfile(User user) throws ExecutionException, InterruptedException {
       return dao.updateProfile(user);
    }
}
