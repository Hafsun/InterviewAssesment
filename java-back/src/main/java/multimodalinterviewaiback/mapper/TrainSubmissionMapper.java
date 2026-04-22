package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.AITraining.TrainSubmission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainSubmissionMapper {
    /**
     * 插入一条用户提交记录
     */
    int insert(TrainSubmission submission);
}