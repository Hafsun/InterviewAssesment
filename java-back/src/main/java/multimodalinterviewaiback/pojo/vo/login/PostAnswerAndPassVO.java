package multimodalinterviewaiback.pojo.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAnswerAndPassVO {
    private String username;
    private String passProtectAnswer;
    private String newPassword;
}
