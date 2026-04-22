package multimodalinterviewaiback.pojo.vo;

import lombok.Data;

@Data
public class ResumeSimpleVO {
    // 个人信息ID（查询简历的唯一标识）
    private Long personalId;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
    // 求职意向职位
    private String jobPosition;
    // 当前所在地
    private String currentLocation;
    // 最后更新时间
    private String updateTime;
}