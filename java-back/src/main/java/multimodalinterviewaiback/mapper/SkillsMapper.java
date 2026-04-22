package multimodalinterviewaiback.mapper;// SkillsMapper.java
import multimodalinterviewaiback.pojo.entity.Skills;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkillsMapper {
    void insert(Skills skills);
    void deleteByPersonalId(Long personalId);
    Skills selectByPersonalId(Long personalId);
}
