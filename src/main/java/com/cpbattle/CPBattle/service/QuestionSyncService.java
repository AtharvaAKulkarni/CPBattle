package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.CFResponseDTO;
import com.cpbattle.CPBattle.entity.Question;
import com.cpbattle.CPBattle.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionSyncService {

    private final QuestionRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public void syncQuestions() throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        CFResponseDTO response = restTemplate.getForObject(
                "https://codeforces.com/api/problemset.problems",
                CFResponseDTO.class
        );

        List<Question> questions = response.getResultDTO().getProblems();

        repository.saveAll(questions);
    }
}
