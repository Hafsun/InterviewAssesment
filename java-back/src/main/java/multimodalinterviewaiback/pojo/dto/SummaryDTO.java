package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 面试总结信息DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SummaryDTO {
    @JsonProperty("average_accuracy_score")
    private String averageAccuracyScore; // 平均准确率得分

    @JsonProperty("expression_pattern")
    private String expressionPattern; // 表情模式

    @JsonProperty("performance_overview")
    private String performanceOverview; // 表现概述
}