package multimodalinterviewaiback.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表情信息明细DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExpressionInfoEntity {
    private int id;
    private int report_id;
    private int round_number;

    private int concentration; // 专注度

    private int confidence; // 自信度


    private int doubt; // 疑惑度

    private int happy; // 快乐度

    private int nervous; // 紧张度
}