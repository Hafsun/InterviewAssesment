package multimodalinterviewaiback.service;

import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.entity.User;
import multimodalinterviewaiback.pojo.entity.UserEntity;
import multimodalinterviewaiback.pojo.vo.login.ReturnGetUserInfo;
import multimodalinterviewaiback.pojo.vo.login.ReturnLoginVO;
import multimodalinterviewaiback.pojo.vo.user.PostUpdatePasswordVO;
import multimodalinterviewaiback.pojo.vo.user.PostUpdateProtectQuestionVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    /**
     * 查询数据库中该用户名是否存在（保证用户名唯一）
     * @param username
     * @return
     */
    int findUserCountByUsername(String username);

    /**
     * 获取用户密码
     * @param username
     * @return
     */
    String getUserPassword(String username);

    /**
     * 添加用户
     * @param a
     */
    void addUser(User a);

    /**
     * 获取用户头像地址
     * @param username
     * @return
     */
    String getUserPictureUrl(String username);

    /**
     * 修改用户头像
     * @param username
     * @param file
     * @return
     */
    String updateUserPicture(String username, MultipartFile file);

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    ReturnGetUserInfo getUserInfo(String username);

    /**
     * 修改密码
     * @param username
     * @param postUpdatePasswordVO
     * @return
     */
    boolean updatePassword(String username, PostUpdatePasswordVO postUpdatePasswordVO);

    /**
     * 修改密保密保
     * @param username
     * @param postUpdateProtectQuestionVO
     * @return
     */
    boolean updateUserProtectQuestion(String username, PostUpdateProtectQuestionVO postUpdateProtectQuestionVO);

    boolean updateUserInfo(UserEntity userEntity);

    Long getUserId(String username, String password);
}
