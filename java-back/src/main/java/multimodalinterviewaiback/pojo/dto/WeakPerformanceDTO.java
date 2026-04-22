package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 弱项表现DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeakPerformanceDTO {
    @JsonProperty("problem_summary")
    private String problemSummary; // 问题总结

    @JsonProperty("round_number")
    private String roundNumber; // 轮次

    @JsonProperty("weakness_analysis")
    private String weaknessAnalysis; // 弱项分析
}