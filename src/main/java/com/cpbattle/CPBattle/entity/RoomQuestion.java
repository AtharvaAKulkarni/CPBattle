package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "room_questions", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "question_id"}))
public class RoomQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private boolean solved=false;
    private Instant firstSolvedAt;

    @ManyToOne
    private RoomUser firstSolvedBy;
}
