package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.entity.QuestionBankFileRenameEntity;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionBankOverviewMapper {
    /**
     * 插入题库总信息
     * @param questionBankVO
     */
    @Insert("INSERT INTO question_bank_overview (title, name, size, url, number, create_time, update_time, module_id) " +
            "VALUES (#{title}, #{name}, #{size}, #{url}, #{number}, #{createTime}, #{updateTime}, #{moduleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 自动回填自增ID到VO的id字段
    void add(QuestionBankVO questionBankVO);

    /**
     * 查询全部信息
     * @return
     */
    @Select("select * from question_bank_overview")
    List<QuestionBankVO> selectAll();

    /**
     * 根据id更换name,moduleId
     * @param questionBankFileRenameEntity
     */
    @Update("update question_bank_overview set name = #{name}, module_id = #{moduleId} where id = #{id}")
    void updateNameById(QuestionBankFileRenameEntity questionBankFileRenameEntity);

    /**
     * 批量删除
     * @param ids
     * @param moduleId
     */
    void deleteBatch(@Param("list") List<Integer> ids, Integer moduleId);
}
