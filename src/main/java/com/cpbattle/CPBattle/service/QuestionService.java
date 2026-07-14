package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.PastSubmissionResponseDTO;
import com.cpbattle.CPBattle.entity.Question;
import com.cpbattle.CPBattle.DTO.QuestionIdentifierDTO;
import com.cpbattle.CPBattle.repository.QuestionRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CodeforcesClient codeforcesClient;

    public ResponseEntity<Question> findQuestionWithRating(Integer rating, String username1, String username2){
        try{
            List<QuestionIdentifierDTO> qUser1 = checkPastSubmissions(username1);
            List<QuestionIdentifierDTO> qUser2 = checkPastSubmissions(username2);
            for (int i = 0; i < 10; i++) {
                Question question = questionRepository.findRandomByRating(rating);
                if (question == null) {
                    throw new Exception("No question found for rating " + rating);
                }
                QuestionIdentifierDTO currQuestion = new QuestionIdentifierDTO();
                currQuestion.setContestId(question.getContestId());
                currQuestion.setIndex(question.getIndex());
                boolean one = qUser1.stream().anyMatch(q -> q.equals(currQuestion));
                boolean two = qUser2.stream().anyMatch(q -> q.equals(currQuestion));
                if (!(one || two)) return new ResponseEntity<>(question, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private List<QuestionIdentifierDTO> checkPastSubmissions(String username) throws Exception {
        return codeforcesClient.getUserSubmissions(username, 30).stream()
                .map(PastSubmissionResponseDTO.ResponseObject::getProblem)
                .distinct()
                .toList();
    }
}
