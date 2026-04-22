package multimodalinterviewaiback.controller;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.pojo.dto.KnowledgeUpdateDTO;
import multimodalinterviewaiback.pojo.dto.KnowledgeUploadDTO;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.vo.Knowledge.KnowledgeResponseVO;
import multimodalinterviewaiback.pojo.vo.Knowledge.KnowledgeVO;
import multimodalinterviewaiback.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin("*")
@Slf4j
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    /**
     * 1. 获取知识库列表
     * GET /knowledge/list
     */
    @GetMapping("/list")
    public Result<List<KnowledgeResponseVO>> getKnowledgeList() {
        List<KnowledgeVO> list = knowledgeService.list();

        // 转换为前端需要的格式
        List<KnowledgeResponseVO> responseList = list.stream()
                .map(KnowledgeResponseVO::from)
                .collect(Collectors.toList());

        return Result.success(responseList);
    }

    /**
     * 2. 上传知识文件
     * POST /knowledge/upload
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String customName,
            @RequestParam("tags") String tagsJson) {
        try {
            log.info("开始上传知识库文件");
            KnowledgeUploadDTO knowledgeUploadDTO = new KnowledgeUploadDTO();
            knowledgeUploadDTO.setFile(file);
            knowledgeUploadDTO.setName(customName);
            knowledgeUploadDTO.setTags(tagsJson);  // 前端传来的 JSON 数组字符串
            knowledgeService.upload(knowledgeUploadDTO);
            return Result.success("文件上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 3. 更新文件信息
     * PUT /knowledge/update/{id}
     */
    @PutMapping("/update/{id}")
    public Result<String> updateKnowledge(
            @PathVariable Integer id,
            @RequestBody KnowledgeUpdateDTO updateDTO) {
        knowledgeService.update(id, updateDTO);
        return Result.success("更新成功");
    }

    /**
     * 4. 删除文件
     * DELETE /knowledge/list/{id}
     */
    @DeleteMapping("/list/{id}")
    public Result<String> deleteKnowledge(@PathVariable Integer id) {
        log.info("接收到删除命令，准备删除知识库表：{}", id);
        knowledgeService.delete(id);
        return Result.success("文件已从宇宙中抹除");
    }
}