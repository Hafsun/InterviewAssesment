package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Long id;
    private String skillName;
    private Integer masteryScore;
}