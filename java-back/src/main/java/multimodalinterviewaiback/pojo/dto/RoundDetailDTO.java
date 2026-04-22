package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 轮次详情DTO（包含表情和评分）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoundDetailDTO {
    @JsonProperty("round_number")
    private int roundNumber; // 轮次编号

    @JsonProperty("expression_info")
    private ExpressionInfoDTO expressionInfoDTO; // 表情信息

    @JsonProperty("integrated_score")
    private IntegratedScoreDTO integratedScoreDTO; // 综合评分
}