package multimodalinterviewaiback.pojo.vo.AITraining;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    private String username;
    private Integer level;
    private Integer exp;
    private Integer consecutiveDays;
    private Integer expToNextLevel;

}
