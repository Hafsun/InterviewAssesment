package multimodalinterviewaiback.pojo.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnLoginVO {
    String jwt;

    String username;
    String name;
    String sex;
    String email;
    String phone;
    String major;
    int interviewCount;
    float comprehensiveScore;
    String pictureUrl;
}
