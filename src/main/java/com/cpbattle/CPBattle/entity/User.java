package com.cpbattle.CPBattle.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("user")
public class User {
    @Id
    private String username;
    private String password;
    private List<String> friends=new ArrayList<>();


}
