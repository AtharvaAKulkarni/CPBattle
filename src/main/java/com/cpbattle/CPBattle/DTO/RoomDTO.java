package com.cpbattle.CPBattle.DTO;

import com.cpbattle.CPBattle.entity.Question;
import com.cpbattle.CPBattle.entity.User;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
public class RoomDTO {
    private String roomId;
    private List<Question> questions=new ArrayList<>();
    private List<User> participants=new ArrayList<>();
    private Instant startTime;
    private Instant endTime;
    private RoomStatus status;
}
