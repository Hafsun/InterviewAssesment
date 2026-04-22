package multimodalinterviewaiback.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表interview_problems对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewProblemEntity {
    private int id;
    @JsonProperty("report_id")
    private int reportId;
    @JsonProperty("round_number")
    private int roundNumber;
    @JsonProperty("question")
    private String question;
    @JsonProperty("answer")
    private String answer;
    @JsonProperty("accuracy_evaluation_id")
    private int accuracyEvaluationId;
    @JsonProperty("expression_analysis_id")
    private int expressionAnalysisId;
}