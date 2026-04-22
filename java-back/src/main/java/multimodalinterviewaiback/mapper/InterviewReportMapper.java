package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.entity.InterviewReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 面试报告实体的Mapper接口
 */
@Mapper
public interface InterviewReportMapper {
    int insert(InterviewReportEntity interviewReportEntity); // 新增报告
    int selectReportId(String beginTime);
    InterviewReportEntity selectById(Integer id); // 根据ID查询

    // 新增：查询用户所有面试分数
    List<Float> selectScoresByUserId(Integer userId);


    /**
     * 根据用户ID分页查询报告
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 报告列表
     */
    List<InterviewReportEntity> selectByUserId(Integer userId, Integer offset, Integer limit);

    /**
     * 统计用户的报告总数
     * @param userId 用户ID
     * @return 总数
     */
    int countByUserId(Integer userId);

    /**
     * 根据报告ID删除面试报告
     * @param reportId 报告ID
     * @return 删除的行数
     */
    int deleteReportById(@Param("reportId") Integer reportId);
}