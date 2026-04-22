package multimodalinterviewaiback.pojo.vo.Knowledge;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class KnowledgeResponseVO {
    private Integer id;
    private String name;
    private String size;
    private String url;
    private String fileType;
    private List<String> tags;  // 前端需要的数组格式
    private LocalDateTime uploadTime;
    private LocalDateTime updateTime;
    private String creator;
    
    // 静态工厂方法，从 KnowledgeVO 转换
    public static KnowledgeResponseVO from(KnowledgeVO vo) {
        KnowledgeResponseVO response = new KnowledgeResponseVO();
        response.setId(vo.getId());
        response.setName(vo.getName());
        response.setSize(vo.getSize());
        response.setUrl(vo.getUrl());
        response.setFileType(vo.getFileType());
        
        // 将字符串 tags 转换为数组
        if (vo.getTags() != null && !vo.getTags().isEmpty()) {
            response.setTags(JSON.parseArray(vo.getTags(), String.class));
        } else {
            response.setTags(List.of());
        }
        
        response.setUploadTime(vo.getUploadTime());
        response.setUpdateTime(vo.getUpdateTime());
        response.setCreator(vo.getCreator());
        
        return response;
    }
}