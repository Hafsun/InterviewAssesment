package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 关键问题（优势/弱项）DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KeyIssuesDTO {
    @JsonProperty("top_performance")
    private List<TopPerformanceDTO> topPerformanceDTOList; // 优势表现列表

    @JsonProperty("weak_performance")
    private List<WeakPerformanceDTO> weakPerformanceDTOList; // 弱项表现列表
}