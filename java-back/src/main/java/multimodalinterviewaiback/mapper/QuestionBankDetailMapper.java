package multimodalinterviewaiback.mapper;


import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionAndAnswer;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionBankDetailMapper {
    /**
     * 批量插入题目
     * @param detailList
     */
    void batchInsert(List<QuestionBankDetail> detailList);

    /**
     * 批量删除题目
     * @param overviewIds
     */
    void deleteByOverviewIds(@Param("overviewIds") List<Integer> overviewIds);

    /**
     * 查询题库下具体题目和答案
     * @param
     * @return
     */
    @Select("select question, answer from question_bank_detail where overview_id = #{id}")
    List<QuestionAndAnswer> selectById(Integer id);
}
