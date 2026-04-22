// ScheduledTask.java
package multimodalinterviewaiback.common;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.service.AiTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {
    
    @Autowired
    private AiTrainingService aiTrainingService;
    
    /**
     * 每天凌晨2点生成今日题目
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailyQuestions() {
        log.info("定时任务：开始生成今日题目");
        try {
            aiTrainingService.generateTodayQuestions();
            log.info("定时任务：今日题目生成完成");
        } catch (Exception e) {
            log.error("定时任务：生成今日题目失败", e);
        }
    }
}