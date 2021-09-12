package com.app.ecommerce.dao;

import com.app.ecommerce.model.Users;
import com.app.ecommerce.repo.EcommerceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ecommerceDao")
public class EcommerceDataAccessService implements EcommerceDao{

    @Autowired
    EcommerceRepository ecommerceRepository;

    @Override
    public Users insertUser(Users users) {
        ecommerceRepository.save(users);
        return users;
    }

    @Override
    public List<Users> getUsersData() {
        return ecommerceRepository.findAll();
    }
}
