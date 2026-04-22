package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverallExpressionInfoDTO {
    private int concentration;
    private int nervousness;
    private int happiness;
    private int doubtfulness;
    private int adaptability;
    private int emotionalStability;
    private int confidence;
}
