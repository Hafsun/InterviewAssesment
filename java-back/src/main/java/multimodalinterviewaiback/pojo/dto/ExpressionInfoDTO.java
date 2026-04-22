package multimodalinterviewaiback.pojo.dto;

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
public class ExpressionInfoDTO {
    @JsonProperty("concentration")
    private int concentration; // 专注度

    @JsonProperty("confidence")
    private int confidence; // 自信度

    @JsonProperty("doubt")
    private int doubt; // 疑惑度

    @JsonProperty("happy")
    private int happy; // 快乐度

    @JsonProperty("nervous")
    private int nervous; // 紧张度
}