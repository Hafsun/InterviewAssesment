package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表interview_summary对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewSummaryEntity {
    private Integer id;
    private Integer reportId;               // 关联的报告ID
    private String averageAccuracyScore;    // 平均准确率得分
    private String expressionPattern;       // 表情模式描述
    private String performanceOverview;     // 整体表现概述
}