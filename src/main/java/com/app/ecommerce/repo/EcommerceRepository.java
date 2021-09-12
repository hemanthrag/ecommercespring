package com.app.ecommerce.repo;

import com.app.ecommerce.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EcommerceRepository extends MongoRepository<Users, Integer> {
}
