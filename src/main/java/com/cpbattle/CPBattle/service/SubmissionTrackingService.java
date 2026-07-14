package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.*;
import com.cpbattle.CPBattle.entity.*;
import com.cpbattle.CPBattle.repository.RoomQuestionRepository;
import com.cpbattle.CPBattle.repository.RoomRepository;
import com.cpbattle.CPBattle.repository.RoomUserRepository;
import com.cpbattle.CPBattle.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionTrackingService {
    @Autowired CodeforcesClient codeforcesClient;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomUserRepository roomUserRepository;
    @Autowired RoomService roomService;
    @Autowired RoomQuestionRepository roomQuestionRepository;
    @Autowired UserService userService;
    @Transactional
    @Scheduled(fixedDelay = 15000)
    public void pollActiveRooms(){
        roomRepository.findByStatusWithQuestions(RoomStatus.RUNNING); // loads questions into session
        List<Room> rooms = roomRepository.findByStatusWithParticipants(RoomStatus.RUNNING); // same rooms, now participants loaded too

        for(Room room: rooms){
            checkTermination(room);
            if(room.getStatus()==RoomStatus.RUNNING) pollRoom(room);
        }
    }

    private void pollRoom(Room room){
        for(RoomUser ru:room.getParticipants()){
            String handle=ru.getUser().getUsername();
            List<PastSubmissionResponseDTO.ResponseObject> subs;
            try{
                subs=codeforcesClient.getUserSubmissions(handle, 10);
            } catch (Exception e) {
                continue;
            }
            for(PastSubmissionResponseDTO.ResponseObject sub:subs){
                if(!"OK".equals(sub.getVerdict())) continue;
                if(sub.getCreationTimeSeconds()<room.getStartTime().getEpochSecond()) continue;

                RoomQuestion rq=matchQuestion(room, sub.getProblem());
                if(rq==null) continue;
                if(submissionRepository.existsByExternalSubmissionId(sub.getId())) continue;
                recordAcceptedSubmission(room, ru, rq, sub);
            }
        }
    }

    private RoomQuestion matchQuestion(Room room, QuestionIdentifierDTO problem) {
        return room.getQuestions().stream()
                .filter(rq -> rq.getQuestion().getContestId().equals(problem.getContestId())
                        && rq.getQuestion().getIndex().equals(problem.getIndex()))
                .findFirst().orElse(null);
    }

    private void recordAcceptedSubmission(Room room, RoomUser ru, RoomQuestion rq, PastSubmissionResponseDTO.ResponseObject sub){
        boolean alreadySolvedByUser = submissionRepository
                .existsByRoomUserAndRoomQuestionAndVerdict(ru, rq, "OK");
        if(alreadySolvedByUser) return;

        Submission submission=new Submission();
        submission.setRoomUser(ru);
        submission.setRoomQuestion(rq);
        submission.setVerdict("OK");
        submission.setSubmittedAt(Instant.now());
        submission.setExternalSubmissionId(sub.getId());
        int points=computePoints(room, rq);
        submission.setPointsAwarded(points);
        submissionRepository.save(submission);

        ru.setScore(ru.getScore()+points);
        ru.setSolvedCount(ru.getSolvedCount()+1);
        ru.setLastSolvedAt(Instant.now());
        roomUserRepository.save(ru);

        if(!rq.isSolved()){
            rq.setSolved(true);
            rq.setFirstSolvedAt(Instant.now());
            rq.setFirstSolvedBy(ru);
            roomQuestionRepository.save(rq);
        }

        RoomEventDTO event=new RoomEventDTO(
                RoomEvent.EventType.SUBMISSION_ACCEPTED,
                ru.getUser().getUsername()+" solved "+rq.getQuestion().getContestId()+rq.getQuestion().getIndex()
        );
        roomService.publish(room.getRoomId(), new SocketResponseDTO<>(SocketResponseDTO.SocketMessageType.ROOM_EVENT, event));
    }

    private int computePoints(Room room, RoomQuestion rq){
        int base=rq.getQuestion().getRating();
        long minutesElapsed= Duration.between(room.getStartTime(), Instant.now()).toMinutes();
        return Math.max(base-(int)minutesElapsed, base/10);
    }

    private void checkTermination(Room room){
        if(room.getStatus()==RoomStatus.ENDED) return;

        boolean timeUp = Instant.now().isAfter(room.getEndTime());
        boolean allSolved = !room.getQuestions().isEmpty()
                && room.getQuestions().stream().allMatch(RoomQuestion::isSolved);

        if(timeUp || allSolved){
            room.setStatus(RoomStatus.ENDED);
            roomRepository.save(room);

            RoomDTO dto = roomService.toRoomDTO(room);
            roomService.publish(room.getRoomId(), new SocketResponseDTO<>(SocketResponseDTO.SocketMessageType.ROOM_UPDATE, dto));

            List<LeaderboardDTO> leaderboard=roomService.getLeaderboard(room.getRoomId());
            userService.updateScore(leaderboard);
            roomService.publish(room.getRoomId(), new SocketResponseDTO<>(SocketResponseDTO.SocketMessageType.LEADERBOARD_UPDATE, leaderboard));
        }
    }
}
