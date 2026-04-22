package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.AITraining.TrainQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrainQuestionMapper {
    /**
     * 根据主键ID查询题目详情
     */
    TrainQuestion findById(@Param("id") Long id);

    /**
     * 根据分类和日期标签获取当天的每日一题
     */
    TrainQuestion findDailyQuestionByCategoryAndTag(@Param("category") String category, @Param("tag") String tag);

    /**
     * 根据创建标识统计题目数量
     */
    int countByCreateTag(@Param("createTag") String createTag);

    /**
     * 根据创建标识删除题目
     */
    void deleteByCreateTag(@Param("createTag") String createTag);

    /**
     * 插入新题目
     */
    void insert(TrainQuestion question);

    /**
     * 批量插入题目
     */
    void batchInsert(@Param("list") List<TrainQuestion> questions);
}