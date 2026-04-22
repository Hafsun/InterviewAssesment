package multimodalinterviewaiback.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 综合评分DTO（用于轮次详情）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IntegratedScoreEntity {
    private int id;
    private int report_id;
    private int round_number;

    private int expression; // 表达能力


    private int logic; // 逻辑思维


    private int major; // 专业知识


    private int stable; // 情绪稳定性


    private int strain; // 应变能力
}