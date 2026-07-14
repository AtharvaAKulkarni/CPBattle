package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private List<String> friends=new ArrayList<>();
    private Integer battlesWon=0;
    private Integer battlesTied=0;
    private Integer battlesLost=0;
}
