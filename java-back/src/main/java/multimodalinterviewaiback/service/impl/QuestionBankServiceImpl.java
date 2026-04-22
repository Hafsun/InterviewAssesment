package multimodalinterviewaiback.service.impl;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.mapper.QuestionBankDetailMapper;
import multimodalinterviewaiback.mapper.QuestionBankOverviewMapper;
import multimodalinterviewaiback.pojo.entity.QuestionBankFileRenameEntity;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionAndAnswer;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankDetail;
import multimodalinterviewaiback.pojo.vo.QuestionBank.QuestionBankVO;
import multimodalinterviewaiback.service.QuestionBankService;
import multimodalinterviewaiback.tools.AliOssTool;
import multimodalinterviewaiback.tools.QuestionBankParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private AliOssTool aliOssTool;
    @Autowired
    private QuestionBankParser questionBankParser;
    @Autowired
    private QuestionBankOverviewMapper questionBankOverviewMapper;
    @Autowired
    private QuestionBankDetailMapper questionBankDetailMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MultipartFile file, Integer moduleId) throws IOException {
        // 1. 校验基础参数
        if (file.isEmpty()) {
            throw new RuntimeException("上传的题库文件不能为空");
        }
        if (moduleId == null || moduleId < 1 || moduleId > 3) {
            throw new RuntimeException("模块ID无效，仅支持1(前端)/2(后端)/3(全栈)");
        }

        // 2. 生成OSS存储的唯一文件名（避免覆盖）
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String objectName = "question-bank/" + dateDir + uuid + ext; // OSS存储路径

        // 3. 上传文件到阿里云OSS
        String fileUrl = aliOssTool.upload(file.getBytes(), objectName);
        log.info("题库文件上传OSS成功：{} -> {}", originalFilename, fileUrl);

        // 4. 解析题库内容（抽取问题和答案）
        Map<String, String> questionAnswerMap = questionBankParser.parseQuestionBank(file);
        log.info("题库解析完成：{}，共抽取 {} 道题目", originalFilename, questionAnswerMap.size());

        // 新增：解析失败（0题）时抛出异常，触发事务回滚
        if (questionAnswerMap.isEmpty()) {
            throw new RuntimeException("题库解析失败，未提取到任何题目，请检查文件格式或联系管理员");
        }

        // 5. 封装题库主信息VO
        QuestionBankVO questionBankVO = new QuestionBankVO();
        questionBankVO.setUrl(fileUrl);
        // 根据模块ID设置标题
        switch (moduleId) {
            case 1 -> questionBankVO.setTitle("前端开发");
            case 2 -> questionBankVO.setTitle("后端开发");
            case 3 -> questionBankVO.setTitle("全栈开发");
            default -> questionBankVO.setTitle("未知模块");
        }
        questionBankVO.setName(originalFilename);
        questionBankVO.setSize(formatFileSize(file.getSize()));
        questionBankVO.setCreateTime(LocalDateTime.now());
        questionBankVO.setModuleId(moduleId);
        questionBankVO.setUpdateTime(LocalDateTime.now());
        // 添加解析的题目数量
        questionBankVO.setNumber(questionAnswerMap.size());
        //插入总表
        questionBankOverviewMapper.add(questionBankVO);
        Integer overviewId = questionBankVO.getId();
        log.info("题库主表插入成功，自增ID：{}", overviewId);

        if(questionAnswerMap.size() > 0){
            List<QuestionBankDetail> detailList = new ArrayList<>();
            for (Map.Entry<String, String> entry : questionAnswerMap.entrySet()) {
                QuestionBankDetail detail = new QuestionBankDetail();
                detail.setQuestion(entry.getKey());
                detail.setAnswer(entry.getValue());
                detail.setOverviewId(overviewId);
                detailList.add(detail);
            }
            questionBankDetailMapper.batchInsert(detailList);
            log.info("题库详情表批量插入成功，共{}条题目", detailList.size());
        }else {
            log.warn("未解析到题目，详情表未插入数据");
        }

    }

    /**
     * 获取题库总表全部数据
     * @return
     */
    @Override
    public List<QuestionBankVO> getAllQuestionBankOverview() {
        List<QuestionBankVO> list = questionBankOverviewMapper.selectAll();
        return list;
    }

    /**
     * 根据题库id更改题库名字及所属模块
     * @param questionBankFileRenameEntity
     */
    @Override
    public void rename(QuestionBankFileRenameEntity questionBankFileRenameEntity) {
        questionBankOverviewMapper.updateNameById(questionBankFileRenameEntity);
    }

    /**
     * 批量上传
     * @param files
     * @param moduleId
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAdd(MultipartFile[] files, Integer moduleId) throws IOException {
        for (MultipartFile file : files) {
            this.add(file,moduleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids, Integer moduleId) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的文件");
        }

        // 1. 删除题库详情表
        questionBankDetailMapper.deleteByOverviewIds(ids);

        // 2. 删除题库总表
        questionBankOverviewMapper.deleteBatch(ids, moduleId);

        log.info("批量删除成功，删除文件数量：{}", ids.size());
    }

    /**
     * 查询题库下具体题目和答案
     * @param fileId
     * @return
     */
    @Override
    public List<QuestionAndAnswer> selectDetail(Integer fileId) {
        log.info("开始查询题库具体题目答案:{}", fileId);
        List<QuestionAndAnswer> questionAndAnswerList = questionBankDetailMapper.selectById(fileId);
        log.info("具体内容查询成功:{}", questionAndAnswerList.size());
        return questionAndAnswerList;
    }

    /**
     * 格式化文件大小（将字节数转为 B/KB/MB 格式）
     * @param size 文件大小（字节）
     * @return 格式化后的大小字符串，如：2.4 MB、980 KB
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            // 小于1KB，显示为 B
            return size + " B";
        } else if (size < 1024 * 1024) {
            // 1KB ~ 1024KB，显示为 KB（保留1位小数）
            return String.format("%.1f KB", (double) size / 1024);
        } else {
            // 大于等于1MB，显示为 MB（保留1位小数）
            return String.format("%.1f MB", (double) size / (1024 * 1024));
        }
    }

}
