package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiEvaluationResultDTO {
    private Integer score;
    private Integer logic;
    private Integer depth;
    private Integer completeness;
    private String comment;
}