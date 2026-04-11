package com.cpbattle.CPBattle.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "question")
public class Question {
    @Id
    private ObjectId id;
    private  Integer contestId;
    private String index;
    private String name;
    private String type;
    private Integer rating;
    private List<String> tags;
}
