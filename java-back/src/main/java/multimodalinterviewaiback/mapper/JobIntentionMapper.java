package multimodalinterviewaiback.mapper;// JobIntentionMapper.java
import multimodalinterviewaiback.pojo.entity.JobIntention;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobIntentionMapper {
    void insert(JobIntention jobIntention);
    void deleteByPersonalId(Long personalId);
    JobIntention selectByPersonalId(Long personalId);
}