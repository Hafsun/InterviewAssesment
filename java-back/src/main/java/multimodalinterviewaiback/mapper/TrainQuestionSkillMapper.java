// TrainQuestionSkillMapper.java
package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.AITraining.TrainQuestionSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TrainQuestionSkillMapper {
    
    /**
     * 根据题目ID查询关联的技能
     */
    List<TrainQuestionSkill> findByQuestionId(@Param("questionId") Long questionId);
    
    /**
     * 插入题目技能关联
     */
    void insert(TrainQuestionSkill questionSkill);
    
    /**
     * 批量插入
     */
    void batchInsert(@Param("list") List<TrainQuestionSkill> list);
    
    /**
     * 根据创建标签删除关联（配合题目删除）
     */
    void deleteByCreateTag(@Param("createTag") String createTag);
}