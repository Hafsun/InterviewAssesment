package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoDTO {
    String name;
    String sex;
    String email;
    String phone;
    String major;
    int interviewCount;
    float comprehensiveScore;
    String pictureUrl;
}
