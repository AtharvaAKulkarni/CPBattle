package com.cpbattle.CPBattle.controller;

import com.cpbattle.CPBattle.DTO.LeaderboardDTO;
import com.cpbattle.CPBattle.DTO.RequestRoomDTO;
import com.cpbattle.CPBattle.DTO.RoomDTO;
import com.cpbattle.CPBattle.entity.Room;
import com.cpbattle.CPBattle.repository.RoomRepository;
import com.cpbattle.CPBattle.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;

    @PostMapping("/create-room")
    public String createRoom(@RequestBody RequestRoomDTO request){
        return roomService.createRoom(request);
    }

    @PostMapping("/join-room/{roomId}")
    public boolean joinRoom(@PathVariable String roomId){
        return roomService.joinRoom(roomId, false);
    }

    @GetMapping("/{roomId}/start")
    public ResponseEntity<?> startRoom(@PathVariable String roomId){
        return roomService.startRoom(roomId);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        System.out.println("Room request recieved.");
        return ResponseEntity.ok(roomService.toRoomDTO(room));
    }

    @GetMapping("/{roomId}/leaderboard")
    public List<LeaderboardDTO> getLeaderboard(@PathVariable String roomId){
        return roomService.getLeaderboard(roomId);
    }

}
