package com.cpbattle.CPBattle.service;

import com.cpbattle.CPBattle.DTO.PastSubmissionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CodeforcesClient {
    @Autowired
    RestTemplate restTemplate;

    public List<PastSubmissionResponseDTO.ResponseObject> getUserSubmissions(String username, int count){
        String url = "https://codeforces.com/api/user.status?handle=" + username + "&from=1&count=" + count;
        ResponseEntity<PastSubmissionResponseDTO> response =
                restTemplate.exchange(url, HttpMethod.GET, null, PastSubmissionResponseDTO.class);

        if (response.hasBody() && response.getBody().getResult() != null) {
            return response.getBody().getResult();
        }
        throw new RuntimeException("No submissions found for handle " + username);
    }
}
