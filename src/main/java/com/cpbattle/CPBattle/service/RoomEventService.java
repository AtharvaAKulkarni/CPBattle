package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.entity.RoomEvent;
import com.cpbattle.CPBattle.repository.RoomEventRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomEventService {
    private final RoomEventRepository roomEventRepository;
    public void save(String roomId, RoomEvent.EventType type, String payload){
        RoomEvent event=new RoomEvent();
        event.setRoomId(roomId);
        event.setEventType(type);
        event.setPayload(payload);
        event.setTimestamp(LocalDateTime.now());
        roomEventRepository.save(event);
    }
    public List<RoomEvent> getHistory(String roomId){
        return roomEventRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
