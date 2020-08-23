package com.amin.sellrenthouse.dao;

import com.amin.sellrenthouse.entities.User;
import com.amin.sellrenthouse.utils.SqlHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserDao  {
      public UserDao() {
    }

    public Exception signUp(final User user) throws ExecutionException, InterruptedException {
        SqlHelper.UserManipulating insertUser = new SqlHelper.UserManipulating();
       String procedure =  "{call insert_user_procedure(?, ?, ?, ?, ?)}";

       return insertUser.execute(procedure, user).get();
      }

    public List<User> signIn(User user) throws ExecutionException, InterruptedException {
        SqlHelper.GetUser getUser = new SqlHelper.GetUser();
        String procedure =  "{call select_user_by_username_and_password_procedure(?, ?)}";

        return getUser.execute(procedure, user).get();
    }

    public List<User> getUser(User user) throws ExecutionException, InterruptedException {
        SqlHelper.GetUser getUser = new SqlHelper.GetUser();
        String procedure =  "{call select_user_by_id_procedure(?)}";

        return getUser.execute(procedure, user).get();
    }

    public Exception removeUser(final User user) throws ExecutionException, InterruptedException {

        SqlHelper.UserManipulating removeUser = new SqlHelper.UserManipulating();
        String procedure =  "{call delete_user_procedure(?)}";

        return removeUser.execute(procedure, user).get();
    }

    public Exception updateProfile(User user) throws ExecutionException, InterruptedException {
        SqlHelper.UserManipulating updateUser = new SqlHelper.UserManipulating();
        String procedure =  "{call update_user_procedure(?, ?, ?, ?, ?)}";

        return updateUser.execute(procedure, user).get();

    }
}