package multimodalinterviewaiback.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {


    /**
     * 根据用户名查询密保问题
     * @param username 用户名
     * @return 密保问题
     */
    String getPassProtectByUsername(String username);

    /**
     * 根据用户名查询密保答案
     * @param username 用户名
     * @return 密保答案
     */
    String getPassProtectAnswerByUsername(String username);

    /**
     * 根据用户名更新密码
     * @param username 用户名
     * @param newPassword 新密码
     * @return 更新的行数
     */
    int updatePasswordByUsername(String username, String newPassword);
}
