package multimodalinterviewaiback.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 前端传入的面试报告完整DTO（顶层对象）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewReportDTO {
    @JsonProperty("username")  // 新增：用户ID
    private String username;

    private LocalDateTime beginTime;

    @JsonProperty("interview_field")  // 新增：面试领域
    private String interviewField;

    @JsonProperty("interview_position")  // 新增：面试岗位
    private String interviewPosition;

    @JsonProperty("interview_position_keyword")  // 新增：岗位关键词
    private String interviewPositionKeyword;

    @JsonProperty("interview_score")  // 新增：面试总分
    private Float interviewScore;

    // 原有字段
    @JsonProperty("overall_report")
    private OverallReportDTO overallReportDTO;

    @JsonProperty("totalInterviewTime")
    private int totalInterviewTime;
}