package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.TopPerformanceDTO;
import multimodalinterviewaiback.pojo.entity.TopPerformanceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 优势表现实体的Mapper接口
 */
@Mapper
public interface TopPerformanceMapper {
    int insert(TopPerformanceEntity topPerformanceEntity); // 新增优势表现
    List<TopPerformanceDTO> getTopPerformanceDTOList(int reportId);

    void deleteByReportId(Integer reportId);
}