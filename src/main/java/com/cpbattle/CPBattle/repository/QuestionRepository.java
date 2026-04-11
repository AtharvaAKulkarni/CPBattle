package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.entity.Question;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, ObjectId> {
    @Aggregation(pipeline = {
            "{$match:{rating:?0}}",
            "{$sample:{size:1}}"
    })
    Question findRandomByRating(Integer rating);
}
