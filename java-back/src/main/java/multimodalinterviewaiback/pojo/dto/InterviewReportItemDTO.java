package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单条面试报告数据DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewReportItemDTO {
    private Integer id;
    private Integer userId;                   // 关联的用户ID
    private String beginTime;                 // 面试开始时间
    private String interviewField;            // 面试领域
    private String interviewPosition;         // 面试岗位
    private String interviewPositionKeyword;  // 岗位关键词
    private Float interviewScore;             // 面试总分
    private Integer totalInterviewTime;       // 总面试时间（秒）
}