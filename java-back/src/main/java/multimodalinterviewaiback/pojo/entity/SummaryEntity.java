package multimodalinterviewaiback.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 面试总结信息DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SummaryEntity {
    private int id;
    private Integer report_id;
    private String averageAccuracyScore; // 平均准确率得分
    private String expressionPattern; // 表情模式
    private String performanceOverview; // 表现概述
}