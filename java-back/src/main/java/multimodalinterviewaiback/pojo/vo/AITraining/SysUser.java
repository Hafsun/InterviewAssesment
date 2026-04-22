package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    private Long id;
    private Integer userId; // 关联user表id
    private String username;
    private Integer level;
    private Integer exp;
    private Integer consecutiveDays;
    private Integer totalSolvedQuestions;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
