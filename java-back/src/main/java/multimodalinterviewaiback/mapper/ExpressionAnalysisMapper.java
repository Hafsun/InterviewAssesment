package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.ExpressionAnalysisDTO;
import multimodalinterviewaiback.pojo.entity.ExpressionAnalysisEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpressionAnalysisMapper {
    int insert(ExpressionAnalysisEntity expressionAnalysisEntity);
    int selectId(int report_id, int round_number);
    ExpressionAnalysisDTO getExpressionAnalysisDTO(int reportId, int roundNumber);

    void deleteByReportId(Integer reportId);
}
