// 在启动类或配置类中调用
// Application.java 或 AppStartupRunner.java
package multimodalinterviewaiback.config;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.service.AiTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartupRunner implements CommandLineRunner {
    
    @Autowired
    private AiTrainingService aiTrainingService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("程序启动，检查今日题目...");
        try {
            aiTrainingService.initTodayQuestions();
            log.info("今日题目检查完成");
        } catch (Exception e) {
            log.error("初始化今日题目失败", e);
        }
    }
}