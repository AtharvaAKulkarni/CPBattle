package com.cpbattle.CPBattle.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketResponseDTO<T> {
    private SocketMessageType type;
    private T payload;

    public enum SocketMessageType{
        ROOM_EVENT,
        ROOM_UPDATE,
        LEADERBOARD_UPDATE
    }
}
