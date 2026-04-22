package multimodalinterviewaiback.pojo.entity;

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
public class ExpressionAnalysisEntity {
    private int id;
    private int report_id;
    private int round_number;

    private String dominantEmotion; // 主导情绪


    private String intensityDuration; // 强度和持续时间


    private String rationalityAnalysis; // 合理性分析
}