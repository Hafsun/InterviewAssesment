package multimodalinterviewaiback.pojo.vo.QuestionBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBankVO {

    private Integer id;
    private Integer moduleId;
    private String title;//所属板块
    private String name;//文件名
    private String size;//文件大小
    private String url;//题库地址
    private Integer number;//该题库题目总数
    private LocalDateTime updateTime;
    private LocalDateTime createTime;

}
