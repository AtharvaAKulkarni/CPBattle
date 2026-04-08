package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
