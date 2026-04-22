// QuestionBankGenerator.java
package multimodalinterviewaiback.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class QuestionBankGenerator {
    // Open API 兼容模式的认证Token
    @Value("${llm.xfyun.api-password}")
    private String apiPassword;

    // 讯飞Spark Lite HTTP接口地址
    @Value("${llm.xfyun.url:https://spark-api-open.xf-yun.com/v1/chat/completions}")
    private String apiUrl;

    /**
     * 生成指定类别的技术面试题
     * @param category 题目类别：frontend/backend/fullstack
     * @return 生成的题目对象
     */
    public GeneratedQuestion generateQuestion(String category) {
        String categoryName = getCategoryName(category);
        log.info("开始使用AI生成{}题目", categoryName);

        try {
            // 1. 构建生成题目的Prompt
            String prompt = buildQuestionPrompt(categoryName);

            // 2. 调用讯飞星火API
            String aiResponse = callXfSparkAPI(prompt);

            // 3. 解析AI返回的题目
            GeneratedQuestion question = parseQuestionResponse(aiResponse, category);

            // ========== 新增：自动识别技能标签 ==========
            List<String> skills = extractSkillsFromQuestion(question.getTitle(), question.getDescription());
            question.setSkills(skills);
            log.info("为题目【{}】识别技能标签：{}", question.getTitle(), skills);
            // =======================================

            log.info("AI生成{}题目成功：{}", categoryName, question.getTitle());
            return question;

        } catch (Exception e) {
            log.error("AI生成{}题目失败，使用备用题目", categoryName, e);
            GeneratedQuestion backupQuestion = getBackupQuestion(category);

            // ========== 备用题目也要识别技能标签 ==========
            List<String> skills = extractSkillsFromQuestion(backupQuestion.getTitle(), backupQuestion.getDescription());
            backupQuestion.setSkills(skills);
            // =======================================

            return backupQuestion;
        }
    }

    /**
     * 批量生成今日三道题目（前端、后端、全栈）
     */
    public List<GeneratedQuestion> generateTodayQuestions() {
        List<GeneratedQuestion> questions = new ArrayList<>();

        // 生成前端题目
        questions.add(generateQuestion("frontend"));

        // 生成后端题目
        questions.add(generateQuestion("backend"));

        // 生成全栈题目
        questions.add(generateQuestion("fullstack"));

        log.info("今日三道题目生成完成，前端：{}，后端：{}，全栈：{}",
                questions.get(0).getTitle(),
                questions.get(1).getTitle(),
                questions.get(2).getTitle()
        );

        return questions;
    }

    /**
     * 根据题目内容识别相关技能标签
     */
    private List<String> extractSkillsFromQuestion(String title, String description) {
        List<String> skills = new ArrayList<>();
        String content = (title + " " + description).toLowerCase();

        // 前端相关技能
        if (content.contains("vue") || content.contains("vue3") || content.contains("组合式api")) {
            skills.add("Vue3");
        }
        if (content.contains("react") || content.contains("hooks") || content.contains("jsx")) {
            skills.add("React");
        }
        if (content.contains("angular") || content.contains("rxjs")) {
            skills.add("Angular");
        }
        if (content.contains("webpack") || content.contains("vite") || content.contains("rollup")) {
            skills.add("前端工程化");
        }
        if (content.contains("typescript") || content.contains("ts")) {
            skills.add("TypeScript");
        }

        // 后端相关技能
        if (content.contains("spring") || content.contains("springboot") || content.contains("ioc") || content.contains("aop")) {
            skills.add("Spring Boot");
        }
        if (content.contains("redis") || content.contains("缓存穿透") || content.contains("缓存雪崩")) {
            skills.add("Redis");
        }
        if (content.contains("mysql") || content.contains("数据库") || content.contains("sql优化") || content.contains("索引")) {
            skills.add("MySQL");
        }
        if (content.contains("mq") || content.contains("消息队列") || content.contains("rabbitmq") || content.contains("kafka")) {
            skills.add("消息队列");
        }
        if (content.contains("jwt") || content.contains("认证") || content.contains("token")) {
            skills.add("JWT认证");
        }

        // 全栈/架构相关技能
        if (content.contains("微服务") || content.contains("springcloud") || content.contains("服务治理")) {
            skills.add("微服务");
        }
        if (content.contains("docker") || content.contains("容器")) {
            skills.add("Docker");
        }
        if (content.contains("k8s") || content.contains("kubernetes")) {
            skills.add("Kubernetes");
        }
        if (content.contains("系统设计") || content.contains("架构设计")) {
            skills.add("系统设计");
        }

        // 去重并限制最多3个核心技能
        List<String> uniqueSkills = new ArrayList<>();
        for (String skill : skills) {
            if (!uniqueSkills.contains(skill)) {
                uniqueSkills.add(skill);
            }
        }

        return uniqueSkills.size() > 3 ? uniqueSkills.subList(0, 3) : uniqueSkills;
    }

    /**
     * 构建生成题目的Prompt（增加技能标签要求）
     */
    private String buildQuestionPrompt(String categoryName) {
        return String.format(
                "请生成一道%s技术面试题（问答题），要求如下：\n\n" +
                        "1. 难度：中等\n" +
                        "2. 题目要结合实际开发场景，具有技术深度\n" +
                        "3. 题目描述要详细清晰，包含具体的问题背景\n" +
                        "4. 提供3-5个考察点提示（JSON数组格式）\n" +
                        "5. 提供详细的参考答案，答案要全面、专业\n" +
                        "6. 生成1000-1500之间的随机热度分\n" +
                        "7. 明确标注这道题主要考察的技术技能（如：Vue3、Spring Boot、Redis等）\n\n" +
                        "请严格按照以下JSON格式返回，不要返回任何多余的文字：\n" +
                        "{\n" +
                        "  \"title\": \"题目标题\",\n" +
                        "  \"description\": \"题目详细描述，200字左右\",\n" +
                        "  \"difficulty\": \"中等\",\n" +
                        "  \"hints\": [\"提示点1\", \"提示点2\", \"提示点3\", \"提示点4\"],\n" +
                        "  \"standardAnswer\": \"标准答案，500字左右，要详细全面\",\n" +
                        "  \"hotScore\": 1200,\n" +
                        "  \"skills\": [\"技能1\", \"技能2\", \"技能3\"]\n" +
                        "}\n\n" +
                        "注意：只返回JSON对象，不要返回其他任何说明文字！\n" +
                        "类别：%s",
                categoryName, categoryName
        );
    }

    /**
     * 调用讯飞星火API
     */
    private String callXfSparkAPI(String prompt) {
        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "lite");
            requestBody.put("messages", new JSONArray() {{
                add(new JSONObject() {{
                    put("role", "user");
                    put("content", prompt);
                }});
            }});
            requestBody.put("temperature", 0.8);  // 提高温度增加创造性
            requestBody.put("stream", false);
            requestBody.put("uid", UUID.randomUUID().toString().substring(0, 10));

            // 发送HTTP请求
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Authorization", "Bearer " + apiPassword)
                    .body(requestBody.toJSONString())
                    .timeout(120000)
                    .setConnectionTimeout(30000)
                    .execute();

            // 检查响应状态
            if (response.getStatus() != 200) {
                String errorMsg = String.format("讯飞API调用失败，状态码：%d，响应：%s",
                        response.getStatus(), response.body());
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 解析响应内容
            JSONObject root = JSON.parseObject(response.body());
            String content = root.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            return content;

        } catch (Exception e) {
            log.error("调用讯飞星火API失败", e);
            throw new RuntimeException("调用讯飞星火API失败：" + e.getMessage());
        }
    }

    /**
     * 解析AI返回的题目JSON
     */
    private GeneratedQuestion parseQuestionResponse(String response, String category) {
        try {
            // 清理返回内容
            String content = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // 提取JSON对象
            int start = content.indexOf("{");
            int end = content.lastIndexOf("}") + 1;
            if (start >= 0 && end > start) {
                content = content.substring(start, end);
            }

            // 解析JSON
            JSONObject jsonObj = JSON.parseObject(content);

            // 构建题目对象
            GeneratedQuestion question = new GeneratedQuestion();
            question.setCategory(category);
            question.setTitle(jsonObj.getString("title"));
            question.setDescription(jsonObj.getString("description"));
            question.setDifficulty(jsonObj.getString("difficulty"));

            // 解析hints数组
            JSONArray hintsArray = jsonObj.getJSONArray("hints");
            if (hintsArray != null && !hintsArray.isEmpty()) {
                List<String> hints = new ArrayList<>();
                for (int i = 0; i < hintsArray.size(); i++) {
                    hints.add(hintsArray.getString(i));
                }
                question.setHints(hints);
            } else {
                question.setHints(getDefaultHints(category));
            }

            question.setStandardAnswer(jsonObj.getString("standardAnswer"));
            question.setHotScore(jsonObj.getInteger("hotScore"));

            // ========== 解析AI返回的技能标签 ==========
            JSONArray skillsArray = jsonObj.getJSONArray("skills");
            if (skillsArray != null && !skillsArray.isEmpty()) {
                List<String> skills = new ArrayList<>();
                for (int i = 0; i < skillsArray.size(); i++) {
                    skills.add(skillsArray.getString(i));
                }
                question.setSkills(skills);
            } else {
                // 如果AI没有返回技能标签，用规则识别
                List<String> skills = extractSkillsFromQuestion(question.getTitle(), question.getDescription());
                question.setSkills(skills);
            }
            // =======================================

            // 验证必要字段
            if (question.getTitle() == null || question.getDescription() == null) {
                log.warn("AI返回的题目字段不完整，使用备用题目");
                return getBackupQuestion(category);
            }

            return question;

        } catch (Exception e) {
            log.error("解析AI返回的题目失败", e);
            return getBackupQuestion(category);
        }
    }

    /**
     * 获取默认的提示点
     */
    private List<String> getDefaultHints(String category) {
        List<String> defaultHints = new ArrayList<>();
        switch (category) {
            case "frontend":
                defaultHints.add("前端性能优化");
                defaultHints.add("组件设计思想");
                defaultHints.add("状态管理");
                defaultHints.add("工程化实践");
                break;
            case "backend":
                defaultHints.add("数据库设计");
                defaultHints.add("缓存策略");
                defaultHints.add("接口设计");
                defaultHints.add("并发处理");
                break;
            case "fullstack":
                defaultHints.add("系统架构设计");
                defaultHints.add("前后端协作");
                defaultHints.add("安全性考虑");
                defaultHints.add("部署运维");
                break;
        }
        return defaultHints;
    }

    /**
     * 获取备用题目（当AI调用失败时使用）
     */
    private GeneratedQuestion getBackupQuestion(String category) {
        GeneratedQuestion question = new GeneratedQuestion();
        question.setCategory(category);
        question.setDifficulty("中等");
        question.setHotScore(1000);

        switch (category) {
            case "frontend":
                question.setTitle("Vue3组合式API的优势");
                question.setDescription("请详细说明Vue3组合式API相比选项式API的优势，以及在实际项目中的应用场景。");
                question.setHints(List.of("逻辑复用", "类型推导", "代码组织", "Tree-shaking"));
                question.setStandardAnswer("Vue3组合式API的优势：\n1. 更好的逻辑复用：通过组合函数可以更方便地抽取和复用逻辑\n2. 更灵活的代码组织：相关代码可以聚合在一起，提高可读性\n3. 更好的TypeScript支持：天然的类型推导\n4. 更小的打包体积：支持Tree-shaking\n5. 更灵活的生命周期管理...");
                break;
            case "backend":
                question.setTitle("Redis缓存雪崩解决方案");
                question.setDescription("请详细说明什么是Redis缓存雪崩，以及有哪些解决方案？");
                question.setHints(List.of("缓存雪崩定义", "过期时间策略", "熔断降级", "数据预热"));
                question.setStandardAnswer("缓存雪崩解决方案：\n1. 设置不同的过期时间，避免同时失效\n2. 使用熔断机制，防止系统崩溃\n3. 构建多级缓存架构\n4. 热点数据提前预热\n5. 使用分布式锁控制数据库访问...");
                break;
            case "fullstack":
                question.setTitle("JWT认证安全实践");
                question.setDescription("请详细说明JWT的工作原理，以及在前端和后端如何安全地实现JWT认证。");
                question.setHints(List.of("JWT结构", "签名算法", "Token刷新", "安全存储"));
                question.setStandardAnswer("JWT安全实践：\n1. 使用强签名算法（HS256/RS256）\n2. 设置合理的过期时间\n3. 实现Token刷新机制\n4. 前端使用httpOnly Cookie存储\n5. 后端验证签名和权限...");
                break;
        }

        return question;
    }

    /**
     * 获取类别中文名称
     */
    private String getCategoryName(String category) {
        switch (category) {
            case "frontend":
                return "前端";
            case "backend":
                return "后端";
            case "fullstack":
                return "全栈";
            default:
                return category;
        }
    }

    /**
     * 生成的题目实体类
     */
    public static class GeneratedQuestion {
        private String category;
        private String title;
        private String description;
        private String difficulty;
        private List<String> hints;
        private String standardAnswer;
        private Integer hotScore;
        private List<String> skills;  // 新增：技能标签列表

        // Getters and Setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

        public List<String> getHints() { return hints; }
        public void setHints(List<String> hints) { this.hints = hints; }

        public String getStandardAnswer() { return standardAnswer; }
        public void setStandardAnswer(String standardAnswer) { this.standardAnswer = standardAnswer; }

        public Integer getHotScore() { return hotScore; }
        public void setHotScore(Integer hotScore) { this.hotScore = hotScore; }

        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
    }

}
