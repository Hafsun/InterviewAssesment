package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.dto.AccuracyEvaluationDTO;
import multimodalinterviewaiback.pojo.entity.AccuracyEvaluationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccuracyEvaluationMapper {
    int insert(AccuracyEvaluationEntity accuracyEvaluationEntity);
    int selectId(int report_id, int round_number);
    AccuracyEvaluationDTO getAccuracyEvaluationDTO(int report_id, int round_number);

    void deleteByReportId(Integer reportId);
}
