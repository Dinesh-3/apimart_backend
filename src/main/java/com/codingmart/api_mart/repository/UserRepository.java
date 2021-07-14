package com.codingmart.api_mart.repository;

import com.codingmart.api_mart.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    User findByName(String name);


}
