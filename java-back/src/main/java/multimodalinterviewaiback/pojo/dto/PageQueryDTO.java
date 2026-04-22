package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询请求参数DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageQueryDTO {
    private String username;     // 用户名（查询条件）
    private Integer pageNum = 1; // 当前页码，默认第1页
    private Integer pageSize = 10; // 每页大小，默认10条
}