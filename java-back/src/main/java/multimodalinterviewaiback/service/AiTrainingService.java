package multimodalinterviewaiback.service;

import multimodalinterviewaiback.pojo.vo.AITraining.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AiTrainingService {

    /**
     * 获取或创建当前用户信息
     * @param userId 当前登录用户ID
     * @return 用户信息DTO
     */
    UserInfoDTO getUserInfo(Long userId);

    /**
     * 根据类别获取今日的挑战题目
     * @param category 题目类别 (frontend/backend/fullstack)
     * @return 每日挑战题目DTO
     */
    DailyQuestionDTO getDailyQuestion(String category);

    /**
     * 获取用户的AI能力图谱
     * @param userId 用户ID
     * @return 技能列表
     */
    List<SkillDTO> getUserSkills(Long userId);

    /**
     * 获取排行榜 (Top 5)
     * @return 排行榜用户列表
     */
    List<LeaderboardUserDTO> getLeaderboard();

    /**
     * 根据ID获取题目详情
     * @param questionId 题目ID
     * @return 题目详情DTO
     */
    QuestionDetailDTO getQuestionDetail(Long questionId);

    /**
     * 提交答案并进行AI评估
     * @param userId 用户ID
     * @param questionId 题目ID
     * @param userAnswer 用户答案
     * @return AI评估结果DTO
     */
    AiEvaluationResultDTO submitAnswerAndEvaluate(Long userId, Long questionId, String userAnswer);

    @Transactional
    void generateTodayQuestions();

    @Transactional
    void checkAndGenerateTodayQuestions();

    @Transactional
    void initTodayQuestions();
}