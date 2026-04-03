package com.csstudy.backend.scheduler;

import com.csstudy.backend.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackScheduler {

    private final SlackService slackService;

    @Value("${slack.enabled}")
    private boolean enabled;

    @Scheduled(cron = "${slack.morning-cron}", zone = "Asia/Seoul")
    public void morningReminder() {
        if (!enabled) {
            return;
        }
        log.info("오전 CS 면접 질문 알림 스케줄러 실행");
        slackService.sendDailyReminder();
    }

    @Scheduled(cron = "${slack.evening-cron}", zone = "Asia/Seoul")
    public void eveningReminder() {
        if (!enabled) {
            return;
        }
        log.info("오후 CS 면접 질문 알림 스케줄러 실행");
        slackService.sendDailyReminder();
    }

    @Scheduled(cron = "${slack.weekly-cron}", zone = "Asia/Seoul")
    public void weeklySummary() {
        if (!enabled) {
            return;
        }
        log.info("주간 CS 학습 리포트 스케줄러 실행");
        slackService.sendWeeklySummary();
    }
}
