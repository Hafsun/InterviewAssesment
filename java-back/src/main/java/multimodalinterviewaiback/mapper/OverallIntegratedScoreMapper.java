package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.entity.OverallExpressionInfoEntity;
import multimodalinterviewaiback.pojo.entity.OverallIntegratedScoreEntity;
import multimodalinterviewaiback.pojo.entity.SummaryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OverallIntegratedScoreMapper {
    int insert(OverallIntegratedScoreEntity overallIntegratedScoreEntity); // 新增综合指标
    OverallIntegratedScoreEntity selectByReportId(Integer reportId); // 根据报告ID查询

    void deleteByReportId(Integer reportId);
}
