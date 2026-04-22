package multimodalinterviewaiback.service;

public interface LoginService {
    /**
     * 获取密保问题
     * @param username 用户名
     * @return 密保问题
     */
    String getPassProtect(String username);

    /**
     * 判断密保答案是否正确
     * @param username 用户名
     * @param passProtectAnswer 密保答案
     * @return 判断结果
     */
    boolean judgePassProtectAnswer(String username, String passProtectAnswer);

    /**
     * 根据密保答案更新密码
     * @param username 用户名
     * @param newPassword 新密码
     * @return 更新结果
     */
    boolean updatePasswordByProtectAnswer(String username, String newPassword);
}
