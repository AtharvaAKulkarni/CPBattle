package com.cpbattle.CPBattle.entity;

import com.cpbattle.CPBattle.DTO.RoomStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String roomId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUser> participants=new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomQuestion> questions=new ArrayList<>();

    private Instant startTime;
    private Instant endTime;

    private Long duration=3600000L;

    private RoomStatus status;
}
