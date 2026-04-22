package multimodalinterviewaiback.service;

import multimodalinterviewaiback.pojo.entity.QuestionBankFileRenameEntity;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionAndAnswer;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionBankService {
    /**
     * 上传题库
     * @param file
     * @param moduleId
     */
    void add(MultipartFile file, Integer moduleId) throws IOException;

    /**
     * 获取题库总表全部数据
     * @return
     */
    List<QuestionBankVO> getAllQuestionBankOverview();

    /**
     * 根据题库id更改题库名字及所属模块
     * @param questionBankFileRenameEntity
     */
    void rename(QuestionBankFileRenameEntity questionBankFileRenameEntity);

    /**
     * 批量上传，TODO此处是多次调用单次上传，未实际完成批量插入
     * @param files
     * @param moduleId
     */
    void batchAdd(MultipartFile[] files, Integer moduleId) throws IOException;

    /**
     * 批量删除
     * @param ids
     * @param moduleId
     */
    void deleteBatch(List<Integer> ids, Integer moduleId);

    /**
     * 查询题库下具体题目和答案
     * @param fileId
     * @return
     */
    List<QuestionAndAnswer> selectDetail(Integer fileId);
}
