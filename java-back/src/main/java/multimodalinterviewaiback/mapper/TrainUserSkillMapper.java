package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.AITraining.TrainUserSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TrainUserSkillMapper {
    /**
     * 根据用户ID查询其所有技能
     */
    List<TrainUserSkill> findByUserId(@Param("userId") Long userId);

    TrainUserSkill findByUserIdAndSkillName(@Param("userId") Long userId, @Param("skillName") String skillName);

    void insert(TrainUserSkill userSkill);

    void update(TrainUserSkill userSkill);

    /**
     * 删除用户所有技能
     */
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除用户指定技能
     */
    void deleteByUserIdAndSkillName(@Param("userId") Long userId, @Param("skillName") String skillName);

    /**
     * 批量插入
     */
    void batchInsert(@Param("list") List<TrainUserSkill> list);

    /**
     * 统计用户技能数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 查询用户掌握度最高的前N个技能
     */
    List<TrainUserSkill> findTopSkills(@Param("userId") Long userId, @Param("limit") int limit);
}