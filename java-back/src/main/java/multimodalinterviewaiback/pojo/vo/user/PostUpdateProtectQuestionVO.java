package multimodalinterviewaiback.pojo.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateProtectQuestionVO {
    String username;
    String password;
    String passProtect;
    String passProtectAnswer;
}
