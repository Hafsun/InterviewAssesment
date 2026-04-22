package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优势表现DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopPerformanceDTO {
    private String problem_summary; // 优势表现描述
    private int round_number;
    private String strengths;
}