package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据库表interview_report对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewReportEntity {
    private Integer id;
    private Integer userId;                   // 关联的用户ID
    private String beginTime;
    private String interviewField;            // 面试领域
    private String interviewPosition;         // 面试岗位
    private String interviewPositionKeyword;  // 岗位关键词
    private Float interviewScore;             // 面试总分
    private Integer totalInterviewTime;       // 总面试时间（秒）
}