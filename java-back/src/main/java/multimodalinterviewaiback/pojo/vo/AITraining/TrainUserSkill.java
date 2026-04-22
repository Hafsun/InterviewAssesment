package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainUserSkill {

    private Long id;
    private Long userId;
    private String skillName;
    private Integer masteryScore;
    private Integer questionCount;     // 练习次数
    private LocalDateTime lastPracticeTime;  // 最后练习时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
