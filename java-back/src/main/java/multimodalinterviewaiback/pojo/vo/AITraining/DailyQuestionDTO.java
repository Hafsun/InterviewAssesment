package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyQuestionDTO {

    private Long id;
    private String title;
    private String difficulty;
    private String description;
    private Integer hotScore;

}
