package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.entity.InterviewSummaryEntity;
import multimodalinterviewaiback.pojo.entity.OverallExpressionInfoEntity;
import multimodalinterviewaiback.pojo.entity.SummaryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OverallExpressionInfoMapper {
    int insert(OverallExpressionInfoEntity overallExpressionInfoEntity); // 新增核心指标
    OverallExpressionInfoEntity selectByReportId(Integer reportId); // 根据报告ID查询

    void deleteByReportId(Integer reportId);
}
