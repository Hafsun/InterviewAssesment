package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterviewReportListDTO {
    private List<InterviewReportItemDTO> reports;  // 报告列表
    private Integer total;                         // 总记录数
    private Integer pageNum;                       // 当前页码
    private Integer pageSize;                      // 每页大小
}