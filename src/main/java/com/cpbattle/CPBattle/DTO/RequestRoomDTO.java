package com.cpbattle.CPBattle.DTO;

import com.cpbattle.CPBattle.entity.User;
import lombok.Data;

import java.util.List;
@Data
public class RequestRoomDTO {
    private List<Integer> ratings;
}
