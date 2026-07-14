package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
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
