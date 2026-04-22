package multimodalinterviewaiback.pojo.vo.AITraining;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainQuestion {

    private Long id;
    private String title;
    private String category; // frontend/backend/fullstack
    private String difficulty; // 简单/中等/困难/地狱
    private String description;
    private String hints; // 考察点/提示，存储JSON字符串形式，例如 "[\"前端性能优化\", \"缓存机制\"]"
    // 如果使用MyBatis并配置了TypeHandler，这里也可以是 List<String>
    private String standardAnswer;
    private Integer hotScore;
    private String createTag; // 创建标识，以当天日期为标识,结构为 2026-4-1
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
