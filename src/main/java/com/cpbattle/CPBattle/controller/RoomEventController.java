package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.DTO.RoomEventDTO;
import com.cpbattle.CPBattle.entity.RoomEvent;
import com.cpbattle.CPBattle.service.RoomEventService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomEventController {
    @Autowired
    RoomEventService roomEventService;
    @GetMapping("/{roomId}/events")
    public List<RoomEventDTO> getHistory(@PathVariable String roomId){
        return roomEventService.getHistory(roomId).stream()
                .map(RoomEventDTO::fromEntity)
                .toList();
    }
}
