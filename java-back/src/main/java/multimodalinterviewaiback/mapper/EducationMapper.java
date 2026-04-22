package multimodalinterviewaiback.mapper;// EducationMapper.java
import multimodalinterviewaiback.pojo.entity.Education;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface EducationMapper {
    void batchInsert(List<Education> educations);
    void deleteByPersonalId(Long personalId);
    List<Education> selectByPersonalId(Long personalId);
}