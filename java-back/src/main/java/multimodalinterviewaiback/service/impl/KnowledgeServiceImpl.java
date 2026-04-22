package multimodalinterviewaiback.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.mapper.KnowledgeOverviewMapper;
import multimodalinterviewaiback.pojo.dto.KnowledgeUpdateDTO;
import multimodalinterviewaiback.pojo.dto.KnowledgeUploadDTO;
import multimodalinterviewaiback.pojo.vo.Knowledge.KnowledgeVO;
import multimodalinterviewaiback.service.KnowledgeService;
import multimodalinterviewaiback.tools.AliOssTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    private KnowledgeOverviewMapper knowledgeOverviewMapper;
    @Autowired
    private AliOssTool aliOssTool;

    @Override
    public List<KnowledgeVO> list() {
        log.info("查询知识库总表返回构建知识库文件列表");
        return knowledgeOverviewMapper.selectAll();
    }

    @Override
    public void upload(KnowledgeUploadDTO knowledgeUploadDTO) {
        MultipartFile file = knowledgeUploadDTO.getFile();
        String customName = knowledgeUploadDTO.getName();
        String tagsJson = knowledgeUploadDTO.getTags();

        // tagsJson 已经是数组格式的字符串，直接存储
        // 例如: ["前端开发","面试题"] 转为字符串存储
        // 生成OSS存储的唯一文件名
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String objectName = "knowledge/" + dateDir + uuid + ext;

        // 上传文件到阿里云OSS
        String fileUrl = null;
        try {
            fileUrl = aliOssTool.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("知识库文件上传OSS成功：{} -> {}", originalFilename, fileUrl);

        KnowledgeVO knowledgeVO = new KnowledgeVO();
        knowledgeVO.setName(customName);
        knowledgeVO.setFileType(customName.substring(customName.lastIndexOf(".") + 1));
        knowledgeVO.setSize(formatFileSize(file.getSize()));
        knowledgeVO.setUrl(fileUrl);
        knowledgeVO.setTags(tagsJson);  // 直接存储 JSON 数组字符串
        knowledgeVO.setUploadTime(LocalDateTime.now());
        knowledgeVO.setUpdateTime(LocalDateTime.now());
        knowledgeVO.setCreator(BaseContext.getCurrentUsername());

        log.info("开始向数据库表中插入数据");
        knowledgeOverviewMapper.insert(knowledgeVO);
        log.info("已成功插入数据");
    }

    @Override
    public void update(Integer id, KnowledgeUpdateDTO updateDTO) {
        log.info("开始更新知识库表");
        updateDTO.setId(id);
        knowledgeOverviewMapper.updateNameAndTagsById(updateDTO);
        log.info("更新知识库表成功");
    }

    @Override
    public void delete(Integer id) {
        log.info("开始删除知识库表{}", id);
        knowledgeOverviewMapper.deleteById(id);
        log.info("删除知识库表成功：{}", id);
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", (double) size / 1024);
        } else {
            return String.format("%.1f MB", (double) size / (1024 * 1024));
        }
    }
}