package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表情分析DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpressionAnalysisDTO {
    @JsonProperty("dominant_emotion")
    private String dominantEmotion; // 主导情绪

    @JsonProperty("intensity_duration")
    private String intensityDuration; // 强度和持续时间

    @JsonProperty("rationality_analysis")
    private String rationalityAnalysis; // 合理性分析
}