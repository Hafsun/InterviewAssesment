package multimodalinterviewaiback.controller;


import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.pojo.dto.QuestionBankDeleteDTO;
import multimodalinterviewaiback.pojo.dto.QuestionBankFileRenameDTO;
import multimodalinterviewaiback.pojo.entity.QuestionBankFileRenameEntity;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.vo.QuestionBank.ModuleFileGroupVO;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionAndAnswer;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankVO;
import multimodalinterviewaiback.service.QuestionBankService;
import multimodalinterviewaiback.tools.AliOssTool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api/question-bank")
@RestController
@CrossOrigin("*")
@Slf4j
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 题库上传
     * @param file
     * @param moduleId
     * @return
     */
    @PostMapping("/upload")
    public Result add(@RequestParam("file") MultipartFile file, @RequestParam("moduleId") Integer moduleId) throws IOException {
        log.info("上传题库文件：{},模块Id:{}", file.getOriginalFilename(), moduleId);
        questionBankService.add(file,moduleId);
        return Result.success();
    }


    /**
     * 查询题库总表
     * @return
     */
    @GetMapping("/list")
    public Result<List<ModuleFileGroupVO>> list(){
        try {
            List<QuestionBankVO> allFiles = questionBankService.getAllQuestionBankOverview();
            // 按moduleId分组
            Map<Integer, List<QuestionBankVO>> groupMap = allFiles.stream()
                    .collect(Collectors.groupingBy(QuestionBankVO::getModuleId));

            // 补充空模块（1/2/3）
            List<ModuleFileGroupVO> moduleFileGroups = new ArrayList<>();
            for (int moduleId = 1; moduleId <= 3; moduleId++) {
                ModuleFileGroupVO group = new ModuleFileGroupVO();
                group.setModuleId(moduleId);
                // 有数据则返回，无数据则返回空列表
                group.setFiles(groupMap.getOrDefault(moduleId, new ArrayList<>()));
                moduleFileGroups.add(group);
            }

            return Result.success("获取文件列表成功", moduleFileGroups);
        } catch (Exception e) {
            log.error("获取文件列表失败",e);
            return Result.error("获取文件列表失败：" + e.getMessage());
        }
    }

    /**
     * 更改题库名字及所属模块
     * @param id
     * @param questionBankFileRenameDTO
     * @return
     */
    @PutMapping("/{id}/rename")
    public Result rename(@PathVariable Integer id, @RequestBody QuestionBankFileRenameDTO questionBankFileRenameDTO) {
        log.info("更改题库:{},{}",id,questionBankFileRenameDTO);
        QuestionBankFileRenameEntity questionBankFileRenameEntity = new QuestionBankFileRenameEntity();
        BeanUtils.copyProperties(questionBankFileRenameDTO, questionBankFileRenameEntity);
        questionBankFileRenameEntity.setId(id);
        questionBankService.rename(questionBankFileRenameEntity);
        return Result.success();
    }

    /**
     * 题库批量上传
     * @param files 多个文件
     * @param moduleId 模块ID
     * @return
     */
    @PostMapping("/batch-upload")
    public Result batchAdd(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("moduleId") Integer moduleId
    ) throws IOException {
        log.info("批量上传题库文件，数量：{}，模块Id:{}", files.length, moduleId);
        questionBankService.batchAdd(files,moduleId);
        return Result.success();
    }

    @DeleteMapping("/batch-delete")
    public Result deleteBatch(@RequestBody QuestionBankDeleteDTO dto) {
        List<Integer> ids = dto.getIds();
        Integer moduleId = dto.getModuleId();

        questionBankService.deleteBatch(ids, moduleId);
        return Result.success();
    }

    /**
     * 查询题库下具体题目和答案
     * @param fileId
     * @return
     */
    @GetMapping("/{fileId}/questions-answers")
    public Result<List<QuestionAndAnswer>> selectDetail(@PathVariable Integer fileId) {
        List<QuestionAndAnswer> questionAndAnswers = questionBankService.selectDetail(fileId);
        return Result.success(questionAndAnswers);
    }

}
