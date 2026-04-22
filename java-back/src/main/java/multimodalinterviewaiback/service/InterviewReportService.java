package multimodalinterviewaiback.service;

import multimodalinterviewaiback.pojo.dto.InterviewReportDTO;
import multimodalinterviewaiback.pojo.dto.InterviewReportListDTO;
import multimodalinterviewaiback.pojo.dto.PageQueryDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 面试报告服务接口
 */
public interface InterviewReportService {
    Integer saveReport(InterviewReportDTO reportDTO); // 保存面试报告
    InterviewReportListDTO getReportListByUsername(PageQueryDTO queryDTO);
    InterviewReportDTO getReportById(Integer reportId); // 查询面试报告

    /**
     * 根据报告ID删除面试报告
     * @param reportId 报告ID
     */
    @Transactional
    void deleteReportById(Integer reportId);
}