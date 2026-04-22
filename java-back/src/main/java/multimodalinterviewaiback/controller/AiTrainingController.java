package multimodalinterviewaiback.controller; // 保持您的包名

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext; // 引入 BaseContext
import multimodalinterviewaiback.pojo.entity.Result; // 引入 Result
import multimodalinterviewaiback.pojo.vo.AITraining.*;
import multimodalinterviewaiback.service.AiTrainingService; // 引入 Service

import org.springframework.beans.factory.annotation.Autowired; // 引入 Autowired
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // 标记为Spring控制器
@ResponseBody // 所有方法默认返回JSON
@CrossOrigin("*") // 允许所有来源的跨域请求
@Slf4j // 使用lombok的Slf4j注解生成logger
@RequestMapping("/api/ai-training")
public class AiTrainingController {

    @Autowired // 通过 @Autowired 注入 Service
    private AiTrainingService aiTrainingService;


    /**
     * 获取当前用户信息（用户名、等级、经验、连续打卡天数）
     * GET /api/ai-training/info
     */
    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo() {
        try {
            Long userId = BaseContext.getCurrentId();// 从 BaseContext 获取
            UserInfoDTO userInfo = aiTrainingService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (RuntimeException e) { // 捕获自定义的运行时异常，例如“用户未登录”
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取用户信息失败, userId: {}", BaseContext.getCurrentId(), e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取每日挑战题目
     * GET /api/ai-training/daily?category=backend
     * @param category 题目类别 (frontend/backend/fullstack)
     */
    @GetMapping("/daily")
    public Result<DailyQuestionDTO> getDailyQuestion(@RequestParam String category) {
        try {
            // 每日挑战可能允许未登录用户查看，这里可以决定是否需要getCurrentUserId()
            // 如果需要：Long userId = getCurrentUserId();
            DailyQuestionDTO question = aiTrainingService.getDailyQuestion(category);
            return Result.success(question);
        } catch (Exception e) {
            log.error("获取每日挑战失败, category: {}", category, e);
            return Result.error("获取每日挑战失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户AI能力图谱
     * GET /api/ai-training/skills
     */
    @GetMapping("/skills")
    public Result<List<SkillDTO>> getUserSkills() {
        try {
            Long userId = BaseContext.getCurrentId(); // 从 BaseContext 获取
            List<SkillDTO> skills = aiTrainingService.getUserSkills(userId);
            return Result.success(skills);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取用户技能图谱失败, userId: {}", BaseContext.getCurrentId(), e);
            return Result.error("获取用户技能图谱失败：" + e.getMessage());
        }
    }

    /**
     * 获取卷王排行榜 (TOP 5)
     * GET /api/ai-training/leaderboard
     */
    @GetMapping("/leaderboard")
    public Result<List<LeaderboardUserDTO>> getLeaderboard() {
        try {
            // 排行榜可能允许未登录用户查看
            List<LeaderboardUserDTO> leaderboard = aiTrainingService.getLeaderboard();
            return Result.success(leaderboard);
        } catch (Exception e) {
            log.error("获取排行榜失败", e);
            return Result.error("获取排行榜失败：" + e.getMessage());
        }
    }

    // --- 挑战界面相关接口 ---

    /**
     * 获取题目详情
     * GET /api/ai-training/detail/{id}
     * @param questionId 题目ID
     */
    @GetMapping("/detail/{questionId}")
    public Result<QuestionDetailDTO> getQuestionDetail(@PathVariable Long questionId) {
        try {
            QuestionDetailDTO detail = aiTrainingService.getQuestionDetail(questionId);
            return Result.success(detail);
        } catch (Exception e) {
            log.error("获取题目详情失败, questionId: {}", questionId, e);
            return Result.error("题目不存在或获取失败：" + e.getMessage());
        }
    }

    /**
     * 提交用户答案并获取AI评估
     * POST /api/ai-training/submit
     * @param request 提交答案请求体
     */
    @PostMapping("/submit")
    public Result<AiEvaluationResultDTO> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        try {
            Long userId = BaseContext.getCurrentId(); // 从 BaseContext 获取
            AiEvaluationResultDTO result = aiTrainingService.submitAnswerAndEvaluate(userId, request.getQuestionId(), request.getUserAnswer());
            return Result.success("提交成功，AI评估完成", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("提交答案或AI评估失败, userId: {}, questionId: {}", BaseContext.getCurrentId(), request.getQuestionId(), e);
            return Result.error("提交答案或AI评估失败：" + e.getMessage());
        }
    }
}