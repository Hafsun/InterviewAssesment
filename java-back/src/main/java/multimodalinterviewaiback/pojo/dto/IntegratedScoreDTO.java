package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 综合评分DTO（用于轮次详情）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IntegratedScoreDTO {
    @JsonProperty("expression")
    private int expression; // 表达能力

    @JsonProperty("logic")
    private int logic; // 逻辑思维

    @JsonProperty("major")
    private int major; // 专业知识

    @JsonProperty("stable")
    private int stable; // 情绪稳定性

    @JsonProperty("strain")
    private int strain; // 应变能力
}