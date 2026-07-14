package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "room_users", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"}))
public class RoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isHost;
    private int solvedCount=0;
    private Instant lastSolvedAt;
    private Integer score=0;
}
