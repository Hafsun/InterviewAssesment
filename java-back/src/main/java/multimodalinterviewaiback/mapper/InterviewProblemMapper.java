package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.entity.InterviewProblemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 面试问题实体的Mapper接口
 */
@Mapper
public interface InterviewProblemMapper {
    int insert(InterviewProblemEntity interviewProblemEntity); // 新增问题
    List<InterviewProblemEntity> selectByReportId(int reportId); // 根据报告ID查询
    /**
     * 根据报告ID删除面试问题
     * @param reportId 报告ID
     * @return 删除的行数
     */
    int deleteByReportId(@Param("reportId") Integer reportId);
}