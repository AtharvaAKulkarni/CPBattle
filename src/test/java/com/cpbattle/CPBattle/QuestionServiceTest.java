package com.cpbattle.CPBattle;

import com.cpbattle.CPBattle.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@SpringBootTest
public class QuestionServiceTest {
    QuestionService questionService=new QuestionService();
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Test
    void test(){
        System.out.println(questionService.findQuestionWithRating(800, "AtharvaK22", "Divy_k"));
    }
}
