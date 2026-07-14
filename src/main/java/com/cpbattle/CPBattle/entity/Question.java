package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private  Integer contestId;
    private String index;
    private String name;
    private String type;
    private Integer rating;
    @ElementCollection
    private List<String> tags;
}
