package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单轮面试分析DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoundAnalysisDTO {

    @JsonProperty("accuracy_evaluation")
    private AccuracyEvaluationDTO accuracyEvaluationDTO; // 准确性评估

    @JsonProperty("answer")
    private String answer; // 回答内容

    @JsonProperty("expression_analysis")
    private ExpressionAnalysisDTO expressionAnalysisDTO; // 表情分析

    @JsonProperty("question")
    private String question; // 问题内容

    @JsonProperty("round_number")
    private int roundNumber; // 轮次编号
}