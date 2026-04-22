package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.AITraining.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysUserMapper {
    /**
     * 根据关联的用户ID (user_id) 查询用户信息
     */
    SysUser findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据主键ID查询用户信息
     */
    SysUser findById(@Param("id") Long id);

    /**
     * 插入一条新的用户信息
     * @return 返回影响的行数
     */
    int insert(SysUser sysUser);

    /**
     * 更新用户信息
     * @return 返回影响的行数
     */
    int update(SysUser sysUser);

    /**
     * 查询排行榜 Top 5 的用户
     */
    List<SysUser> findTop5ByOrderByTotalSolvedDesc();
}