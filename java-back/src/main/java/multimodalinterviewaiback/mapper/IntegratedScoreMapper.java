package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.IntegratedScoreDTO;
import multimodalinterviewaiback.pojo.entity.IntegratedScoreEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 综合评分实体的Mapper接口
 */
@Mapper
public interface IntegratedScoreMapper {
    int insert(IntegratedScoreEntity integratedScoreEntity); // 新增评分
    IntegratedScoreEntity selectById(Integer id); // 根据ID查询
    IntegratedScoreDTO getIntegratedScoreDTO(int report_id, int round_number);

    void deleteByReportId(Integer reportId);
}