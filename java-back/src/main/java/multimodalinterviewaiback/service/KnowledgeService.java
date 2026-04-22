package multimodalinterviewaiback.service;

import multimodalinterviewaiback.pojo.dto.KnowledgeUpdateDTO;
import multimodalinterviewaiback.pojo.dto.KnowledgeUploadDTO;
import multimodalinterviewaiback.pojo.vo.Knowledge.KnowledgeVO;

import java.util.List;

public interface KnowledgeService {

    /**
     * 查询知识库总表
     * @return
     */
    List<KnowledgeVO> list();


    /**
     * 上传知识库总表
     * @param knowledgeUploadDTO
     */
    void upload(KnowledgeUploadDTO knowledgeUploadDTO);

    /**
     * 根据id更新name和tags
     * @param id
     * @param updateDTO
     */
    void update(Integer id, KnowledgeUpdateDTO updateDTO);

    /**
     * 根据id删除知识库信息
     * @param id
     */
    void delete(Integer id);
}
