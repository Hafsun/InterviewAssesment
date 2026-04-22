// AiTrainingServiceImpl.java
package multimodalinterviewaiback.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.mapper.*;
import multimodalinterviewaiback.pojo.vo.AITraining.*;
import multimodalinterviewaiback.service.AiTrainingService;
import multimodalinterviewaiback.tools.QuestionBankGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiTrainingServiceImpl implements AiTrainingService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private TrainQuestionMapper trainQuestionMapper;

    @Autowired
    private TrainSubmissionMapper trainSubmissionMapper;

    @Autowired
    private TrainUserSkillMapper trainUserSkillMapper;

    @Autowired
    private TrainQuestionSkillMapper trainQuestionSkillMapper;  // 新增：题目技能关联Mapper

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionBankGenerator questionBankGenerator;

    private static final int EXP_PER_LEVEL = 1000;

    // ========== 现有方法保持不变 ==========

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        // ... 保持不变 ...
        SysUser sysUser = sysUserMapper.findByUserId(userId);
        if (sysUser == null) {
            log.info("新用户首次访问AI训练模块，为其创建档案. userId: {}", userId);
            sysUser = new SysUser();
            sysUser.setUserId(userId.intValue());
            sysUser.setUsername(BaseContext.getCurrentUsername());
            sysUser.setLevel(1);
            sysUser.setExp(0);
            sysUser.setConsecutiveDays(0);
            sysUser.setTotalSolvedQuestions(0);
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUserMapper.insert(sysUser);
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(sysUser, userInfoDTO);
        int currentLevel = sysUser.getLevel();
        int expNeededForNextLevel = currentLevel * EXP_PER_LEVEL;
        userInfoDTO.setExpToNextLevel(expNeededForNextLevel);
        return userInfoDTO;
    }

    @Override
    public DailyQuestionDTO getDailyQuestion(String category) {
        String todayTag = LocalDate.now().toString();
        ensureTodayQuestionsExist();  // 懒加载确保题目存在
        TrainQuestion question = trainQuestionMapper.findDailyQuestionByCategoryAndTag(category, todayTag);
        if (question == null) {
            log.warn("今日({})暂无类别为 '{}' 的挑战题目", todayTag, category);
            return null;
        }
        DailyQuestionDTO dto = new DailyQuestionDTO();
        BeanUtils.copyProperties(question, dto);
        return dto;
    }

    @Override
    public List<SkillDTO> getUserSkills(Long userId) {
        List<TrainUserSkill> skills = trainUserSkillMapper.findByUserId(userId);
        if (skills == null || skills.isEmpty()) {
            log.info("用户暂无技能数据，返回空列表。userId: {}", userId);
            return new ArrayList<>();
        }
        return skills.stream()
                .filter(skill -> skill.getMasteryScore() > 0)
                .sorted((a, b) -> b.getMasteryScore() - a.getMasteryScore())
                .map(skill -> {
                    SkillDTO dto = new SkillDTO();
                    BeanUtils.copyProperties(skill, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardUserDTO> getLeaderboard() {
        List<SysUser> topUsers = sysUserMapper.findTop5ByOrderByTotalSolvedDesc();
        if (topUsers == null || topUsers.isEmpty()) {
            return new ArrayList<>();
        }
        return topUsers.stream().map(user -> {
            LeaderboardUserDTO dto = new LeaderboardUserDTO();
            dto.setUserId(user.getUserId().longValue());
            dto.setUsername(user.getUsername());
            dto.setTotalSolved(user.getTotalSolvedQuestions());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public QuestionDetailDTO getQuestionDetail(Long questionId) {
        TrainQuestion question = trainQuestionMapper.findById(questionId);
        if (question == null) {
            throw new RuntimeException("题目不存在！");
        }
        QuestionDetailDTO dto = new QuestionDetailDTO();
        BeanUtils.copyProperties(question, dto);
        try {
            List<String> hintsList = objectMapper.readValue(question.getHints(), new TypeReference<List<String>>() {});
            dto.setHints(hintsList);
        } catch (Exception e) {
            log.error("解析题目 hints JSON 失败, questionId: {}", questionId, e);
            dto.setHints(List.of());
        }
        return dto;
    }

    // ========== 核心修改：提交答案并更新技能 ==========
    @Override
    @Transactional
    public AiEvaluationResultDTO submitAnswerAndEvaluate(Long userId, Long questionId, String userAnswer) {
        // 1. 获取题目信息
        TrainQuestion question = trainQuestionMapper.findById(questionId);
        if (question == null) {
            throw new RuntimeException("提交的题目不存在！");
        }

        // 2. AI评估（模拟或真实）
        AiEvaluationResultDTO evaluationResult = mockAiEvaluation(userAnswer, question.getStandardAnswer());

        // 3. 保存提交记录
        TrainSubmission submission = new TrainSubmission();
        submission.setUserId(userId);
        submission.setQuestionId(questionId);
        submission.setUserAnswer(userAnswer);
        submission.setAiScore(evaluationResult.getScore());
        submission.setAiLogicScore(evaluationResult.getLogic());
        submission.setAiDepthScore(evaluationResult.getDepth());
        submission.setAiCompletenessScore(evaluationResult.getCompleteness());
        submission.setAiComment(evaluationResult.getComment());
        submission.setSubmitTime(LocalDateTime.now());
        trainSubmissionMapper.insert(submission);

        // 4. 更新用户经验值和等级
        SysUser sysUser = sysUserMapper.findByUserId(userId);
        if (sysUser != null) {
            // ========== 新增：更新连续打卡天数 ==========
            updateConsecutiveDays(sysUser);
            // ========================================
            sysUser.setTotalSolvedQuestions(sysUser.getTotalSolvedQuestions() + 1);
            int gainedExp = 50 + (evaluationResult.getScore() / 2);
            int currentExp = sysUser.getExp() + gainedExp;

            while (currentExp >= sysUser.getLevel() * EXP_PER_LEVEL) {
                currentExp -= sysUser.getLevel() * EXP_PER_LEVEL;
                sysUser.setLevel(sysUser.getLevel() + 1);
                log.info("恭喜用户 {} 升级到 Lv.{}!", sysUser.getUsername(), sysUser.getLevel());
            }
            sysUser.setExp(currentExp);
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUserMapper.update(sysUser);
        }

        // ========== 5. 核心新增：更新用户技能图谱 ==========
        updateUserSkills(userId, questionId, evaluationResult.getScore(), question.getDifficulty());

        return evaluationResult;
    }


    /**
     * 更新用户连续打卡天数
     */
    private void updateConsecutiveDays(SysUser sysUser) {
        LocalDate today = LocalDate.now();
        LocalDate lastPracticeDate = sysUser.getUpdateTime() != null
                ? sysUser.getUpdateTime().toLocalDate()
                : null;

        int currentDays = sysUser.getConsecutiveDays();

        if (lastPracticeDate == null) {
            // 第一次打卡
            sysUser.setConsecutiveDays(1);
            log.info("用户首次打卡，连续天数：1");
        } else if (lastPracticeDate.equals(today)) {
            // 今天已经打卡过，不增加
            log.info("用户今天已打卡，连续天数不变：{}", currentDays);
        } else if (lastPracticeDate.equals(today.minusDays(1))) {
            // 昨天有打卡，连续天数+1
            sysUser.setConsecutiveDays(currentDays + 1);
            log.info("用户连续打卡，连续天数：{} → {}", currentDays, currentDays + 1);
        } else {
            // 中断了，重置为1
            sysUser.setConsecutiveDays(1);
            log.info("用户打卡中断，重置连续天数为：1");
        }
    }
    /**
     * 更新用户技能图谱
     */
    private void updateUserSkills(Long userId, Long questionId, int aiScore, String difficulty) {
        // 1. 获取题目关联的技能列表
        List<TrainQuestionSkill> questionSkills = trainQuestionSkillMapper.findByQuestionId(questionId);

        if (questionSkills == null || questionSkills.isEmpty()) {
            log.warn("题目 {} 没有关联技能标签，跳过技能更新", questionId);
            return;
        }

        // 2. 计算难度系数
        double difficultyFactor = getDifficultyFactor(difficulty);
        log.info("题目难度：{}，难度系数：{}", difficulty, difficultyFactor);

        // 3. 更新每个技能
        for (TrainQuestionSkill qs : questionSkills) {
            String skillName = qs.getSkillName();
            Double weight = qs.getWeight();  // 题目对该技能的权重
            if (weight == null) weight = 1.0;

            // 计算本次技能得分 = AI评分 × 权重 × 难度系数
            int currentSkillScore = (int) (aiScore * weight * difficultyFactor);
            currentSkillScore = Math.min(currentSkillScore, 100);  // 最高100分
            currentSkillScore = Math.max(currentSkillScore, 0);    // 最低0分

            log.info("技能 {}：AI评分={}, 权重={}, 难度系数={}, 本次得分={}",
                    skillName, aiScore, weight, difficultyFactor, currentSkillScore);

            // 获取用户当前技能记录
            TrainUserSkill userSkill = trainUserSkillMapper.findByUserIdAndSkillName(userId, skillName);

            if (userSkill == null) {
                // 首次获得该技能
                userSkill = new TrainUserSkill();
                userSkill.setUserId(userId);
                userSkill.setSkillName(skillName);
                userSkill.setMasteryScore(currentSkillScore);
                userSkill.setQuestionCount(1);
                userSkill.setLastPracticeTime(LocalDateTime.now());
                trainUserSkillMapper.insert(userSkill);
                log.info("用户获得新技能：{}，初始分数：{}", skillName, currentSkillScore);
            } else {
                // 更新已有技能（加权平均）
                int oldScore = userSkill.getMasteryScore();
                int oldCount = userSkill.getQuestionCount();

                // 新分数 = (旧分数 × 旧次数 + 本次分数) / (旧次数 + 1)
                int newScore = (oldScore * oldCount + currentSkillScore) / (oldCount + 1);

                userSkill.setMasteryScore(newScore);
                userSkill.setQuestionCount(oldCount + 1);
                userSkill.setLastPracticeTime(LocalDateTime.now());
                trainUserSkillMapper.update(userSkill);

                log.info("技能 {} 更新：{} → {} (练习次数：{})",
                        skillName, oldScore, newScore, oldCount + 1);
            }
        }
    }

    /**
     * 根据难度获取系数
     */
    private double getDifficultyFactor(String difficulty) {
        switch (difficulty) {
            case "简单": return 0.8;
            case "中等": return 1.0;
            case "困难": return 1.2;
            case "地狱": return 1.5;
            default: return 1.0;
        }
    }

    /**
     * AI评估模拟
     */
    private AiEvaluationResultDTO mockAiEvaluation(String userAnswer, String standardAnswer) {
        try {
            Thread.sleep(1500 + (long)(Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int score = 30, logic = 40, depth = 35, completeness = 45;
        if (userAnswer.length() > 50) { score += 20; logic += 15; completeness += 10; }
        if (userAnswer.length() > 150) { score += 20; logic += 20; completeness += 20; depth += 15; }
        if (userAnswer.contains("Redis") || userAnswer.contains("缓存")) { score += 10; depth += 15; }
        if (userAnswer.contains("MQ") || userAnswer.contains("消息队列")) { score += 10; depth += 15; }
        if (userAnswer.contains("首先") && userAnswer.contains("其次") && userAnswer.contains("最后")) { logic += 25; }

        score = Math.min(score, 98);
        logic = Math.min(logic, 98);
        depth = Math.min(depth, 98);
        completeness = Math.min(completeness, 98);

        AiEvaluationResultDTO result = new AiEvaluationResultDTO();
        result.setScore(score);
        result.setLogic(logic);
        result.setDepth(depth);
        result.setCompleteness(completeness);
        result.setComment("这是一个模拟的AI点评。您的回答结构清晰，提及了关键技术点。如果能进一步阐述细节，表现会更完美。");
        return result;
    }

    // ========== 题目生成相关方法 ==========

    @Override
    @Transactional
    public void generateTodayQuestions() {
        String todayTag = LocalDate.now().toString();
        log.info("开始生成今日题目，日期标识: {}", todayTag);

        int existingCount = trainQuestionMapper.countByCreateTag(todayTag);
        if (existingCount >= 3) {
            log.info("今日已存在3道题目，跳过生成。现有数量: {}", existingCount);
            return;
        }

        if (existingCount > 0) {
            log.info("今日已有{}道题目，将删除后重新生成", existingCount);
            trainQuestionMapper.deleteByCreateTag(todayTag);
            // 同时删除旧的题目技能关联
            trainQuestionSkillMapper.deleteByCreateTag(todayTag);
        }

        try {
            List<QuestionBankGenerator.GeneratedQuestion> generatedQuestions =
                    questionBankGenerator.generateTodayQuestions();

            for (QuestionBankGenerator.GeneratedQuestion generated : generatedQuestions) {
                // 保存题目
                TrainQuestion question = new TrainQuestion();
                question.setTitle(generated.getTitle());
                question.setDescription(generated.getDescription());
                question.setDifficulty(generated.getDifficulty());
                question.setCategory(generated.getCategory());
                question.setHotScore(generated.getHotScore());
                question.setCreateTag(todayTag);
                question.setCreateTime(LocalDateTime.now());
                question.setUpdateTime(LocalDateTime.now());
                question.setStandardAnswer(generated.getStandardAnswer());

                try {
                    String hintsJson = objectMapper.writeValueAsString(generated.getHints());
                    question.setHints(hintsJson);
                } catch (Exception e) {
                    log.error("转换hints为JSON失败", e);
                    question.setHints("[]");
                }

                trainQuestionMapper.insert(question);
                log.info("保存题目成功：{} - {}，ID：{}", generated.getCategory(), generated.getTitle(), question.getId());

                // ========== 保存题目-技能关联 ==========
                if (generated.getSkills() != null && !generated.getSkills().isEmpty()) {
                    for (String skillName : generated.getSkills()) {
                        TrainQuestionSkill qs = new TrainQuestionSkill();
                        qs.setQuestionId(question.getId());
                        qs.setSkillName(skillName);
                        qs.setWeight(1.0);  // 默认权重为1
                        trainQuestionSkillMapper.insert(qs);
                        log.info("题目关联技能：{} -> {}", question.getId(), skillName);
                    }
                }
            }

            log.info("今日三道题目生成并保存完成");

        } catch (Exception e) {
            log.error("生成今日题目失败", e);
            throw new RuntimeException("生成今日题目失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void checkAndGenerateTodayQuestions() {
        String todayTag = LocalDate.now().toString();
        int count = trainQuestionMapper.countByCreateTag(todayTag);
        if (count < 3) {
            log.info("今日题目不足3道，当前数量：{}，开始生成", count);
            generateTodayQuestions();
        } else {
            log.info("今日题目已存在，无需生成。数量：{}", count);
        }
    }

    // AiTrainingServiceImpl.java
    @Override
    @Transactional
    public void initTodayQuestions() {
        String todayTag = LocalDate.now().toString();
        int count = trainQuestionMapper.countByCreateTag(todayTag);

        if (count == 0) {
            log.info("程序启动检测到今日无题目，开始生成...");
            generateTodayQuestions();
        } else if (count < 3) {
            log.info("程序启动检测到今日题目不足3道（当前{}道），开始补充生成...", count);
            generateTodayQuestions();
        } else {
            log.info("程序启动检测到今日题目已存在（{}道），无需生成", count);
        }
    }

    /**
     * 确保今日题目存在（懒加载）
     */
    @Transactional
    public synchronized void ensureTodayQuestionsExist() {
        String todayTag = LocalDate.now().toString();
        int count = trainQuestionMapper.countByCreateTag(todayTag);
        if (count == 0) {
            log.info("检测到今日无题目，开始生成...");
            generateTodayQuestions();
        } else if (count < 3) {
            log.info("检测到今日题目不足（{}/3），开始补充...", count);
            generateTodayQuestions();
        }
    }
}