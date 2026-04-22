package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表user对应的实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {
    private Integer id;
    private String username;            // 用户名
    private String password;            // 密码（加密存储）
    private String passProtect;         // 密保问题
    private String passProtectAnswer;   // 密保答案
    private String pictureUrl;          // 头像URL
    private String name;                // 真实姓名
    private String sex;                 // 性别
    private String email;               // 邮箱
    private String phone;               // 手机号
    private String major;               // 专业
    private Integer interviewCount;     // 面试次数
    private Float comprehensiveScore;   // 综合评分
}