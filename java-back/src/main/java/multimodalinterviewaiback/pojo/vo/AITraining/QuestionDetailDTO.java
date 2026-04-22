package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailDTO {
    private Long id;
    private String title;
    private String difficulty;
    private String description;
    private List<String> hints; // 注意：这里是List<String>，业务层需要将实体中的String类型JSON转换过来
    private String standardAnswer;
}