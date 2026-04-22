package multimodalinterviewaiback.controller;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.pojo.dto.PageQueryDTO;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.entity.Resume;
import multimodalinterviewaiback.service.impl.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody // 返回JSON
@CrossOrigin("*") // 允许跨域
@Slf4j
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    // 保存简历
    @PostMapping("/save")
    public Result saveResume(@RequestBody Resume resume) {
        try {
            resumeService.saveResume(resume);
            return Result.success("简历保存成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 删除简历
    @DeleteMapping("/delete/{id}")
    public Result deleteResume(@PathVariable Long id) {
        try {
            resumeService.deleteResume(id);
            return Result.success("简历删除成功", null);
        } catch (Exception e) {
            return Result.error("简历删除失败：" + e.getMessage());
        }
    }

    // 查询简历
    @GetMapping("/{id}")
    public Result getResume(@PathVariable Long id) {
        if(resumeService.getResume(id) == null) {
            return Result.error("该简历不存在");
        }else {
            return Result.success(resumeService.getResume(id));
        }
    }

    // 新增：分页查询简历基本信息
    @PostMapping("/page")
    public Result getSimpleInfoByPage(@RequestBody PageQueryDTO queryDTO) {
        try{
            return Result.success(resumeService.getSimpleInfoByPage(queryDTO.getPageNum(), queryDTO.getPageSize(), queryDTO.getUsername()));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }
}