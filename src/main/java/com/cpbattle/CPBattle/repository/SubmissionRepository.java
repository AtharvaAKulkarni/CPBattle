package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.entity.RoomQuestion;
import com.cpbattle.CPBattle.entity.RoomUser;
import com.cpbattle.CPBattle.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByExternalSubmissionId(Long externalSubmissionId);
    boolean existsByRoomUserAndRoomQuestionAndVerdict(RoomUser ru, RoomQuestion rq, String verdict);
}