package multimodalinterviewaiback.mapper;// ProjectMapper.java
import multimodalinterviewaiback.pojo.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProjectMapper {
    void batchInsert(List<Project> projects);
    void deleteByPersonalId(Long personalId);
    List<Project> selectByPersonalId(Long personalId);
}