package com.cpbattle.CPBattle.DTO;

import com.cpbattle.CPBattle.entity.RoomEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomEventDTO {
    private RoomEvent.EventType eventType;
    private String payload;

    public static RoomEventDTO fromEntity(RoomEvent entity) {
        return new RoomEventDTO(
                entity.getEventType(),
                entity.getPayload()
        );
    }
}
