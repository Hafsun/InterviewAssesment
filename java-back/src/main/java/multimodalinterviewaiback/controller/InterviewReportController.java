package multimodalinterviewaiback.controller;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.pojo.dto.InterviewReportDTO;
import multimodalinterviewaiback.pojo.dto.InterviewReportListDTO;
import multimodalinterviewaiback.pojo.dto.PageQueryDTO;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.service.InterviewReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 面试报告控制器
 */
@Controller
@ResponseBody // 返回JSON
@CrossOrigin("*") // 允许跨域
@Slf4j
@RequestMapping("/api/report")
public class InterviewReportController {
    @Autowired
    private InterviewReportService interviewReportService;

    // 保存面试报告
    @PostMapping("/save")
    public Result saveReport(@RequestBody InterviewReportDTO reportPayload) {
        try {
            Integer reportId = interviewReportService.saveReport(reportPayload);
            log.info("保存成功");
            return Result.success("报告保存成功", null);
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }


    /**
     * 分页查询用户的面试报告
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @PostMapping("/interviewList")
    public Result getReportListByUsername(@RequestBody PageQueryDTO queryDTO) {
        InterviewReportListDTO interviewReportListDTO = new InterviewReportListDTO();
        queryDTO.setUsername(BaseContext.getCurrentUsername());
        interviewReportListDTO = interviewReportService.getReportListByUsername(queryDTO);
        try {
            if(interviewReportListDTO != null) {
                return Result.success(interviewReportListDTO);
            }else{
                return Result.error("无报告");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    // 查询用户面试报告
    @GetMapping("/getReport/{id}")
    public Result getReport(@PathVariable Integer id) {
        try {
            InterviewReportDTO reportDTO = interviewReportService.getReportById(id);
            if (reportDTO == null) {
                return Result.error("报告不存在");
            }
            return Result.success("查询成功", reportDTO);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reportId}")
    public Result deleteReport(@PathVariable Integer reportId) {
        try {
            interviewReportService.deleteReportById(reportId);
            log.info("报告删除成功，报告ID：{}", reportId);
            return Result.success("报告删除成功", null);
        } catch (Exception e) {
            log.error("报告删除失败，报告ID：{}，错误信息：{}", reportId, e.getMessage());
            return Result.error("报告删除失败：" + e.getMessage());
        }
    }
}