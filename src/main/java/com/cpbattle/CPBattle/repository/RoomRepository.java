package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.DTO.RoomStatus;
import com.cpbattle.CPBattle.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(String roomId);

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.questions WHERE r.status = :status")
    List<Room> findByStatusWithQuestions(@Param("status") RoomStatus status);

    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN FETCH r.participants WHERE r.status = :status")
    List<Room> findByStatusWithParticipants(@Param("status") RoomStatus status);
}
