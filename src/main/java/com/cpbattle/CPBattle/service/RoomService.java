package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.*;
import com.cpbattle.CPBattle.entity.*;
import com.cpbattle.CPBattle.repository.QuestionRepository;
import com.cpbattle.CPBattle.repository.RoomRepository;
import com.cpbattle.CPBattle.repository.RoomUserRepository;
import com.cpbattle.CPBattle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    RoomEventService roomEventService;
    @Autowired
    RoomUserRepository roomUserRepository;

    //HELPER FUNCTIONS

    // Generates a roomId for a room
    private static String generate() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length=7;
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    //Publishes to socket connection
    public void publish(String roomId, SocketResponseDTO<?> response){
        if (response.getType() == SocketResponseDTO.SocketMessageType.ROOM_EVENT) {
            RoomEventDTO event = (RoomEventDTO) response.getPayload();
            roomEventService.save(
                    roomId,
                    event.getEventType(),
                    event.getPayload()
            );
        }
        simpMessagingTemplate.convertAndSend("/topic/room/"+roomId, response);
    }

    //Converts a room to roomDTO to hide unimportant features
    public RoomDTO toRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();

        dto.setRoomId(room.getRoomId());
        dto.setStatus(room.getStatus());
        dto.setStartTime(room.getStartTime());
        dto.setEndTime(room.getEndTime());

        for (RoomQuestion question : room.getQuestions()) {
            dto.getQuestions().add(question.getQuestion());
        }

        for (RoomUser user : room.getParticipants()) {
            dto.getParticipants().add(user.getUser());
        }

        return dto;
    }

    //Creates a room and internally calls join
    public String createRoom(RequestRoomDTO request){
        Room room=new Room();
        room.setRoomId(generate());

        List<Integer> ratings=request.getRatings();
        for(Integer rating: ratings){
            RoomQuestion room_question=new RoomQuestion();
            room_question.setRoom(room);
            Question question=questionRepository.findRandomByRating(rating);
            room_question.setQuestion(question);
            room.getQuestions().add(room_question);
        }
        Room savedRoom=roomRepository.save(room);
        joinRoom(savedRoom.getRoomId(), true);
        return savedRoom.getRoomId();
    }

    //Joins a user to a room
    public boolean joinRoom(String roomId, boolean isHost){
        Room room=roomRepository.findByRoomId(roomId);
        if(room==null){
            return false;
        }

        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);

        boolean alreadyJoined = room.getParticipants().stream()
                .anyMatch(rp -> rp.getUser().getId().equals(user.getId()));
        if (alreadyJoined) {
            return false;
        }

        RoomUser room_user=new RoomUser();
        room_user.setRoom(room);
        room_user.setUser(user);
        room_user.setHost(isHost);
        room.getParticipants().add(room_user);

        roomRepository.save(room);

        RoomEventDTO event=new RoomEventDTO(
                RoomEvent.EventType.USER_JOINED,
                user.getUsername()
        );

        publish(roomId, new SocketResponseDTO<>(SocketResponseDTO.SocketMessageType.ROOM_EVENT, event));

        return true;
    }

    //Publishes the room object on the socket connection
    public ResponseEntity<?> startRoom(String roomId) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        String hostUsername=roomUserRepository.findHostUserIdByRoomId(roomId);
        if(Objects.equals(username, hostUsername)){
            Room room=roomRepository.findByRoomId(roomId);
            room.setStartTime(Instant.now());
            room.setEndTime(Instant.now().plus(Duration.ofMillis(room.getDuration())));
            room.setStatus(RoomStatus.RUNNING);

            roomRepository.save(room);
            System.out.println(room.getQuestions());
            System.out.println(room.getQuestions().size());
            System.out.println(room.getQuestions().getFirst());
            publish(roomId, new SocketResponseDTO<>(SocketResponseDTO.SocketMessageType.ROOM_UPDATE, toRoomDTO(room)));

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //Returns the leaderboard
    public List<LeaderboardDTO> getLeaderboard(String roomId){
        return roomUserRepository.getLeaderboard(roomId);
    }
}
