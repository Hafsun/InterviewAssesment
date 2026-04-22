package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表top_performance对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopPerformanceEntity {
    private Integer id;
    private Integer report_id;
    private String problem_summary; // 优势表现描述
    private int round_number;
    private String strengths;
}