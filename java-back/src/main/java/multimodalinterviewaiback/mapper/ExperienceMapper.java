package multimodalinterviewaiback.mapper;// ExperienceMapper.java
import multimodalinterviewaiback.pojo.entity.Experience;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ExperienceMapper {
    void batchInsert(List<Experience> experiences);
    void deleteByPersonalId(Long personalId);
    List<Experience> selectByPersonalId(Long personalId);
}