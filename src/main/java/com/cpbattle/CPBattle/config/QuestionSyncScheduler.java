package com.cpbattle.CPBattle.config;

import com.cpbattle.CPBattle.service.QuestionSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionSyncScheduler {
    private final QuestionSyncService questionSyncService;

    @Scheduled(cron = "0 0 3 * * SUN") // Every Sunday at 3 AM
    public void syncQuestions() throws Exception {
        questionSyncService.syncQuestions();
    }
}
