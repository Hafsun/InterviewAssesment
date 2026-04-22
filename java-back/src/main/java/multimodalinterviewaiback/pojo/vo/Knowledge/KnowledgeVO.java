package multimodalinterviewaiback.pojo.vo.Knowledge;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeVO {

    private Integer id;
    private String name;
    private String size;
    private String url;
    private String fileType;
    private String tags;  // 数据库存储的是字符串
    private LocalDateTime uploadTime;
    private LocalDateTime updateTime;
    private String creator;

    // 添加一个辅助方法，用于返回给前端的数组格式
    public List<String> getTagsList() {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        return JSON.parseArray(tags, String.class);
    }

    // 设置时接收数组，自动转为字符串存储
    public void setTagsList(List<String> tagsList) {
        if (tagsList == null || tagsList.isEmpty()) {
            this.tags = null;
        } else {
            this.tags = JSON.toJSONString(tagsList);
        }
    }
}