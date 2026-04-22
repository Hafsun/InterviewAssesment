package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回答准确性评估DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccuracyEvaluationDTO {
    @JsonProperty("completeness")
    private String completeness; // 完整性
    @JsonProperty("logical_rigor")
    private String logicalRigor; // 逻辑严谨性
    @JsonProperty("matching_degree")
    private String matchingDegree; // 匹配度
    @JsonProperty("score")
    private int score; // 得分
    @JsonProperty("technical_correctness")
    private String technical_correctness; // 技术正确性
}