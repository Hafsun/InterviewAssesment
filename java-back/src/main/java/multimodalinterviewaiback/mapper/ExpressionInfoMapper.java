package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.ExpressionAnalysisDTO;
import multimodalinterviewaiback.pojo.dto.ExpressionInfoDTO;
import multimodalinterviewaiback.pojo.entity.ExpressionInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 面部表情实体的Mapper接口
 */
@Mapper
public interface ExpressionInfoMapper {
    int insert(ExpressionInfoEntity expressionInfoEntity); // 新增表情数据
//    FaceExpressionEntity selectById(Integer id); // 根据ID查询
    ExpressionInfoDTO getExpressionInfoDTO(int report_id, int round_number);

    void deleteByReportId(Integer reportId);
}