package multimodalinterviewaiback.pojo.vo.login;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegisterVO {
    private String username;
    private String password;

    private String passProtect;
    private String passProtectAnswer;
}
