package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private RoomUser roomUser;
    @ManyToOne private RoomQuestion roomQuestion;

    private String verdict;
    private Instant submittedAt;
    private Long externalSubmissionId;
    private Integer pointsAwarded;
}
