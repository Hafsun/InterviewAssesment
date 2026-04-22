package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainSubmission {

    private Long id;
    private Long userId;
    private Long questionId;
    private String userAnswer;
    private Integer aiScore;
    private Integer aiLogicScore;
    private Integer aiDepthScore;
    private Integer aiCompletenessScore;
    private String aiComment;
    private LocalDateTime submitTime;


}
