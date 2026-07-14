package com.cpbattle.CPBattle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="room_events")
public class RoomEvent {

    @Id
    @GeneratedValue
    private Long id;
    private String roomId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private LocalDateTime timestamp;

    public enum EventType{
        USER_JOINED,
        USER_LEFT,
        MATCH_STARTED,
        MATCH_ENDED,
        CHAT_MESSAGE,
        ROOM_ENDED, SUBMISSION_ACCEPTED
    }
}
