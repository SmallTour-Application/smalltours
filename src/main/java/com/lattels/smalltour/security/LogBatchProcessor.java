
package com.lattels.smalltour.security;


import com.lattels.smalltour.dto.LogMemberDTO;
import com.lattels.smalltour.model.LogMember;
import com.lattels.smalltour.persistence.LogMemberRepository;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * logMember테이블에 들어갈 값 메모리에 저장해두고
 * 일정 시간 지나면 DB에 저장되게함
 */
@Component
public class LogBatchProcessor {

    private static final int BATCH_SIZE = 1;
    private final List<LogMember> logBuffer = new ArrayList<>();
    private final LogMemberRepository logMemberRepository;
    private final List<LogMember> failedLogs = new ArrayList<>();

    @Autowired
    public LogBatchProcessor(LogMemberRepository logMemberRepository) {
        this.logMemberRepository = logMemberRepository;
    }

    public synchronized void addLog(LogMember log) {
        logBuffer.add(log);
        if (logBuffer.size() >= BATCH_SIZE) {
            flushLogs();
        }
    }

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    public synchronized void flushLogs() {
        try {
            if (!logBuffer.isEmpty()) {
                logMemberRepository.saveAll(logBuffer);
                logBuffer.clear();
            }
            retryFailedLogs();
        } catch (Exception e) {
            failedLogs.addAll(new ArrayList<>(logBuffer));
            logBuffer.clear();
            // 로깅 또는 알림 메커니즘을 사용하여 오류를 기록합니다.

        }
    }

    private void retryFailedLogs() {
        if (!failedLogs.isEmpty()) {
            try {
                logMemberRepository.saveAll(failedLogs);
                failedLogs.clear();
            } catch (Exception e) {
                // 재시도 중 실패한 로그에 대한 추가적인 처리

            }
        }
    }
}
