package multimodalinterviewaiback.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 讯飞星火Open API 题库解析客户端（适配Hutool 5.8.22，无编译错误）
 */
@Slf4j
@Component
public class LLMQuestionBankClient {

    // Open API 兼容模式的认证Token
    @Value("${llm.xfyun.api-password}")
    private String apiPassword;

    // 讯飞Spark Lite HTTP接口地址
    @Value("${llm.xfyun.url:https://spark-api-open.xf-yun.com/v1/chat/completions}")
    private String apiUrl;

    /**
     * 核心方法：和你原始代码接口完全一致
     */
    public Map<String, String> parseByLLM(String fileText) {
        // 空文本直接返回空Map
        if (fileText == null || fileText.trim().isEmpty()) {
            log.warn("LLM解析失败：待解析文本为空");
            return new HashMap<>();
        }

        try {
            // 1. 构建解析Prompt（复用你原始逻辑）
            String prompt = buildQuestionBankPrompt(fileText);

            // 2. 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "lite");
            requestBody.put("messages", new JSONArray() {{
                add(new JSONObject() {{
                    put("role", "user");
                    put("content", prompt);
                }});
            }});
            requestBody.put("temperature", 0.01);
            requestBody.put("stream", false);
            requestBody.put("uid", UUID.randomUUID().toString().substring(0, 10));

            // 3. 发送HTTP请求（适配Hutool 5.8.22的方法名）
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Authorization", "Bearer " + apiPassword)
                    .body(requestBody.toJSONString())
                    .timeout(120000) // 总超时2分钟（包含连接+读取）
                    // 关键修正：Hutool 5.8.22 用 setConnectionTimeout 而非 connectionTimeout
                    .setConnectionTimeout(30000)
                    .execute();

            // 4. 检查响应状态
            if (response.getStatus() != 200) {
                String errorMsg = String.format("讯飞API调用失败，状态码：%d，响应：%s",
                        response.getStatus(), response.body());
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 5. 解析返回结果（复用你原始逻辑）
            Map<String, String> resultMap = parseXfResponse(response.body());
            log.info("LLM解析完成，提取题目数量：{}", resultMap.size());
            return resultMap;

        } catch (Exception e) {
            log.error("讯飞LLM解析题库失败", e);
            throw new RuntimeException("讯飞星火API解析失败：" + e.getMessage());
        }
    }

    /**
     * 构建精准的题库解析Prompt（完全复用你原始代码）
     */
    private String buildQuestionBankPrompt(String fileText) {
        return "请严格按照以下规则解析题库内容：\n" +
                "1. 仅提取【题干】和【答案】两部分内容，忽略解析、选项、注释等无关信息；\n" +
                "2. 输出格式必须是标准JSON对象，题干作为Key，答案作为Value，不要返回任何多余文字；\n" +
                "3. 选择题答案仅保留选项字母（如A、B、AB），不要返回完整选项内容；\n" +
                "4. 去除题干和答案前后的多余空格，确保每个题干唯一；\n" +
                "5. 若未提取到任何题目，返回空JSON对象 {}；\n" +
                "6. 还原所有转义字符（如\\u003c替换为<，\\u003e替换为>）；\n" +
                "需要解析的题库内容：\n" + fileText;
    }

    /**
     * 解析讯飞返回结果（完全复用你原始代码）
     */
    private Map<String, String> parseXfResponse(String responseBody) {
        Map<String, String> resultMap = new HashMap<>();

        try {
            JSONObject root = JSON.parseObject(responseBody);
            String content = root.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            content = content.replace("```json", "")
                    .replace("```", "")
                    .replace("\\u003c", "<")
                    .replace("\\u003e", ">")
                    .trim();

            if (content.startsWith("{") && content.endsWith("}")) {
                JSONObject jsonObj = JSON.parseObject(content);
                for (String key : jsonObj.keySet()) {
                    String question = key.trim();
                    String answer = jsonObj.getString(key).trim();
                    if (!question.isEmpty() && !answer.isEmpty()) {
                        resultMap.put(question, answer);
                    }
                }
            } else {
                log.warn("LLM返回非JSON格式内容：{}", content);
            }

        } catch (Exception e) {
            log.error("解析讯飞返回结果失败", e);
            throw new RuntimeException("解析LLM返回结果失败：" + e.getMessage());
        }

        return resultMap;
    }
}