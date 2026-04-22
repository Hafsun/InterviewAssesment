package multimodalinterviewaiback.pojo.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdatePasswordVO {
    String username;
    String oldPassword;
    String newPassword;
}
