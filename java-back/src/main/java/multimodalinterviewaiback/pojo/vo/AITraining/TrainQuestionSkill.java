// TrainQuestionSkill.java
package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.Data;

@Data
public class TrainQuestionSkill {
    private Long id;
    private Long questionId;
    private String skillName;
    private Double weight;  // 权重 0-1，默认1
}