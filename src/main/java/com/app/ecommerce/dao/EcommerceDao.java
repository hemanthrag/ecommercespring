package com.app.ecommerce.dao;

import com.app.ecommerce.model.Users;

import java.util.List;

public interface EcommerceDao {
    Users insertUser( Users users);

    default Users addUser(Users users){
        return insertUser(users);
    }

    List<Users> getUsersData();
}
