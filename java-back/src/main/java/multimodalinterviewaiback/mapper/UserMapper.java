package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.dto.GetUserInfoDTO;
import multimodalinterviewaiback.pojo.entity.User;
import multimodalinterviewaiback.pojo.entity.UserEntity;
import multimodalinterviewaiback.pojo.vo.user.PostUpdateProtectQuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {
    /**
     * 查询数据库中该用户名是否存在（保证用户名唯一）
     * @param username
     * @return
     */
    int findUserCountByUsername(String username);
    String getUserPassword(String username);
    void addUser(User a);
    int getUserIdByUsername(String username);
    String getPictureUrlByUsername(String username);
    int updatePictureUrl(String username, String pictureUrl);
    GetUserInfoDTO getUserInfo(String username);
    String findUserPassword(String username);
    int updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);
    int updateUserProtectQuestion(String username, @Param("info") PostUpdateProtectQuestionVO postUpdateProtectQuestion);


    // 根据ID查询用户（获取当前面试次数和分数）
    UserEntity selectById(Integer id);

    // 更新用户的面试次数和平均分数
    int updateInterviewStats(UserEntity userEntity);

    /**
     * 更新用户信息
     */
    int updateUserInfo(UserEntity user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    UserEntity selectByUsername(String username);

    @Select("select id from user where username = #{username} and password = #{password}")
    Long selectId(String username, String password);
}
