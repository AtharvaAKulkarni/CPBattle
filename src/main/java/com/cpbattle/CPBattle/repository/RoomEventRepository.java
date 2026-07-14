package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.entity.RoomEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomEventRepository extends JpaRepository<RoomEvent, Long> {
    List<RoomEvent> findByRoomIdOrderByTimestampAsc(String roomId);
}
