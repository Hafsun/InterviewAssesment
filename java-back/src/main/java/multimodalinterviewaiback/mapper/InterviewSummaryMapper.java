package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.SummaryDTO;
import multimodalinterviewaiback.pojo.entity.InterviewSummaryEntity;
import multimodalinterviewaiback.pojo.entity.SummaryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 面试总结实体的Mapper接口
 */
@Mapper
public interface InterviewSummaryMapper {
    int insert(SummaryEntity summaryEntity); // 新增总结
    InterviewSummaryEntity selectByReportId(Integer reportId); // 根据报告ID查询

    void deleteByReportId(Integer reportId);
}