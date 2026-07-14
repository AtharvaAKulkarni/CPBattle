package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.DTO.LeaderboardDTO;
import com.cpbattle.CPBattle.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface RoomUserRepository extends JpaRepository<RoomUser, Integer> {
    @Query("SELECT ru.user.username FROM RoomUser ru WHERE ru.room.roomId= :roomId AND ru.isHost=true")
    String findHostUserIdByRoomId(@Param("roomId") String roomId);

    @Query("SELECT new  com.cpbattle.CPBattle.DTO.LeaderboardDTO(ru.user.username, ru.score) FROM RoomUser ru WHERE ru.room.roomId= :roomId ORDER BY ru.score DESC")
    List<LeaderboardDTO> getLeaderboard(@Param("roomId") String roomId);
}
