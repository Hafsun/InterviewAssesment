package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.dto.WeakPerformanceDTO;
import multimodalinterviewaiback.pojo.entity.WeakPerformanceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 弱项表现实体的Mapper接口
 */
@Mapper
public interface WeakPerformanceMapper {
    int insert(WeakPerformanceEntity weakPerformanceEntity); // 新增弱项表现
    List<WeakPerformanceDTO> getWeakPerformanceDTOList(int reportId);

    void deleteByReportId(Integer reportId);
}