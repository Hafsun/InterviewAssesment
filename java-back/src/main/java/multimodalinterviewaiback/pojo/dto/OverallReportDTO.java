package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 整体报告信息DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverallReportDTO {
    @JsonProperty("summary")
    private SummaryDTO summaryDTO; // 总结信息

    @JsonProperty("key_issues")
    private KeyIssuesDTO keyIssuesDTO; // 关键问题（优势/弱项）

    @JsonProperty("round_analysis")
    private List<RoundAnalysisDTO> roundAnalysisDTOList;

    @JsonProperty("roundDetails")
    private List<RoundDetailDTO> roundDetailDTOList;

    @JsonProperty("overallIntegratedScore")
    private OverallIntegratedScoreDTO overallIntegratedScoreDTO;

    @JsonProperty("overallExpressionInfo")
    private OverallExpressionInfoDTO overallExpressionInfoDTO;

}