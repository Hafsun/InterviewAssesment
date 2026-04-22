package multimodalinterviewaiback.service.impl;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.mapper.*;
import multimodalinterviewaiback.pojo.entity.Honors;
import multimodalinterviewaiback.pojo.entity.JobIntention;
import multimodalinterviewaiback.pojo.entity.Resume;
import multimodalinterviewaiback.pojo.entity.Skills;
import multimodalinterviewaiback.pojo.vo.ResumeSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Service
public class ResumeService {

    @Autowired
    private PersonalInfoMapper personalInfoMapper;
    @Autowired
    private JobIntentionMapper jobIntentionMapper;
    @Autowired
    private EducationMapper educationMapper;
    @Autowired
    private ExperienceMapper experienceMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private SkillsMapper skillsMapper;
    @Autowired
    private HonorsMapper honorsMapper;

    // 保存简历（事务管理）
    @Transactional
    public void saveResume(Resume resume) {
        // 保存主表
        resume.getPersonalInfo().setUsername(BaseContext.getCurrentUsername());
        personalInfoMapper.insert(resume.getPersonalInfo());
        Long personalId = resume.getPersonalInfo().getId();


        // 设置关联ID并保存子表
        JobIntention jobIntention = resume.getJobIntention();
        jobIntention.setPersonalId(personalId);
        jobIntentionMapper.insert(jobIntention);

        Skills skills = resume.getSkills();
        skills.setPersonalId(personalId);
        skillsMapper.insert(skills);

        Honors honors = resume.getHonors();
        honors.setPersonalId(personalId);
        honorsMapper.insert(honors);

        // 批量保存列表数据
        resume.getEducations().forEach(edu -> edu.setPersonalId(personalId));
        educationMapper.batchInsert(resume.getEducations());

        resume.getExperiences().forEach(exp -> exp.setPersonalId(personalId));
        experienceMapper.batchInsert(resume.getExperiences());

        resume.getProjects().forEach(proj -> proj.setPersonalId(personalId));
        projectMapper.batchInsert(resume.getProjects());
    }

    // 删除简历（级联删除）
    @Transactional
    public void deleteResume(Long personalId) {
        // 删除子表
        jobIntentionMapper.deleteByPersonalId(personalId);
        educationMapper.deleteByPersonalId(personalId);
        experienceMapper.deleteByPersonalId(personalId);
        projectMapper.deleteByPersonalId(personalId);
        skillsMapper.deleteByPersonalId(personalId);
        honorsMapper.deleteByPersonalId(personalId);
        // 删除主表
        personalInfoMapper.deleteById(personalId);
    }

    // 查询简历
    public Resume getResume(Long personalId) {
        Resume resume = new Resume();
        resume.setPersonalInfo(personalInfoMapper.selectById(personalId));
        resume.setJobIntention(jobIntentionMapper.selectByPersonalId(personalId));
        resume.setEducations(educationMapper.selectByPersonalId(personalId));
        resume.setExperiences(experienceMapper.selectByPersonalId(personalId));
        resume.setProjects(projectMapper.selectByPersonalId(personalId));
        resume.setSkills(skillsMapper.selectByPersonalId(personalId));
        resume.setHonors(honorsMapper.selectByPersonalId(personalId));
        return resume;
    }

    // 新增：分页查询简历基本信息
    public Map<String, Object> getSimpleInfoByPage(Integer pageNum, Integer pageSize, String username) {
        // 计算分页偏移量（pageNum从1开始）
        Integer offset = (pageNum - 1) * pageSize;
        // 查询当前页数据
        List<ResumeSimpleVO> dataList = personalInfoMapper.selectSimpleInfoByPage(offset, pageSize, username);
        // 查询总记录数
        Integer totalCount = personalInfoMapper.selectTotalCount();
        // 计算总页数
        Integer totalPage = (totalCount + pageSize - 1) / pageSize;

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("dataList", dataList);       // 当前页数据
        result.put("totalCount", totalCount);   // 总记录数
        result.put("totalPage", totalPage);     // 总页数
        result.put("currentPage", pageNum);     // 当前页码
        result.put("pageSize", pageSize);       // 每页条数
        return result;
    }
}