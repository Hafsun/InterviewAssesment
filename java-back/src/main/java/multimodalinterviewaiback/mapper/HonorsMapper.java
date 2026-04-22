package multimodalinterviewaiback.mapper;// HonorsMapper.java
import multimodalinterviewaiback.pojo.entity.Honors;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HonorsMapper {
    void insert(Honors honors);
    void deleteByPersonalId(Long personalId);
    Honors selectByPersonalId(Long personalId);
}