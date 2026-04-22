package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表weak_performance对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeakPerformanceEntity {
    private Integer id;
    private Integer reportId;        // 关联的报告ID
    private String problemSummary;   // 问题总结
    private String roundNumber;      // 涉及轮次
    private String weaknessAnalysis; // 弱项分析
}