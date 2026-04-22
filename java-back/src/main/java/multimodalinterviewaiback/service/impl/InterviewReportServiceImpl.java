package multimodalinterviewaiback.service.impl;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.pojo.dto.*;
import multimodalinterviewaiback.pojo.entity.*;
import multimodalinterviewaiback.mapper.*;
import multimodalinterviewaiback.service.InterviewReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 面试报告服务实现类
 */
@Service
@Component
@Slf4j
public class InterviewReportServiceImpl implements InterviewReportService {

    @Autowired
    private InterviewReportMapper interviewReportMapper;
    @Autowired
    private InterviewProblemMapper interviewProblemMapper;
    @Autowired
    private ExpressionInfoMapper expressionInfoMapper;
    @Autowired
    private IntegratedScoreMapper integratedScoreMapper;
    @Autowired
    private TopPerformanceMapper topPerformanceMapper;
    @Autowired
    private WeakPerformanceMapper weakPerformanceMapper;
    @Autowired
    private InterviewSummaryMapper interviewSummaryMapper;
    @Autowired
    private AccuracyEvaluationMapper accuracyEvaluationMapper;
    @Autowired
    ExpressionAnalysisMapper expressionAnalysisMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OverallExpressionInfoMapper overallExpressionInfoMapper;
    @Autowired
    private OverallIntegratedScoreMapper overallIntegratedScoreMapper;

    @Override
    @Transactional
    public Integer saveReport(InterviewReportDTO reportDTO) {
        // 1. 保存主报告实体（假设reportDTO中包含userId）
        Integer userId = userMapper.getUserIdByUsername(reportDTO.getUsername()); // 从DTO获取用户ID
        InterviewReportEntity reportEntity = new InterviewReportEntity();
        reportEntity.setUserId(userId);
        reportEntity.setBeginTime(reportDTO.getBeginTime().toString());
        reportEntity.setInterviewField(reportDTO.getInterviewField());
        reportEntity.setInterviewPosition(reportDTO.getInterviewPosition());
        reportEntity.setInterviewPositionKeyword(reportDTO.getInterviewPositionKeyword());
        reportEntity.setInterviewScore(reportDTO.getInterviewScore()); // 本次面试分数
        reportEntity.setTotalInterviewTime(reportDTO.getTotalInterviewTime());

        interviewReportMapper.insert(reportEntity);
        Integer reportId = interviewReportMapper.selectReportId(reportEntity.getBeginTime());


        // 2. 保存overall_report
        OverallReportDTO overallReportDTO = reportDTO.getOverallReportDTO();

        // 2.1 保存key_issues
        KeyIssuesDTO keyIssuesDTO = overallReportDTO.getKeyIssuesDTO();
        for(TopPerformanceDTO topPerformance : keyIssuesDTO.getTopPerformanceDTOList()){
            TopPerformanceEntity topPerformanceEntity = new TopPerformanceEntity();
            topPerformanceEntity.setReport_id(reportId);
            topPerformanceEntity.setStrengths(topPerformance.getStrengths());
            topPerformanceEntity.setRound_number(topPerformance.getRound_number());
            topPerformanceEntity.setProblem_summary(topPerformance.getProblem_summary());

            topPerformanceMapper.insert(topPerformanceEntity);
        }
        for(WeakPerformanceDTO weakPerformance : keyIssuesDTO.getWeakPerformanceDTOList()){
            WeakPerformanceEntity weakPerformanceEntity = new WeakPerformanceEntity();
            weakPerformanceEntity.setReportId(reportId);
            weakPerformanceEntity.setWeaknessAnalysis(weakPerformance.getWeaknessAnalysis());
            weakPerformanceEntity.setRoundNumber(weakPerformance.getRoundNumber());
            weakPerformanceEntity.setProblemSummary(weakPerformance.getProblemSummary());

            weakPerformanceMapper.insert(weakPerformanceEntity);
        }

        //2.2 保存summary
        SummaryDTO summaryDTO = overallReportDTO.getSummaryDTO();
        SummaryEntity summaryEntity = new SummaryEntity();
        summaryEntity.setReport_id(reportId);
        summaryEntity.setExpressionPattern(summaryDTO.getExpressionPattern());
        summaryEntity.setPerformanceOverview(summaryDTO.getPerformanceOverview());
        summaryEntity.setAverageAccuracyScore(summaryDTO.getAverageAccuracyScore());

        interviewSummaryMapper.insert(summaryEntity);


        // 2.3 保存round_analysis
        for(RoundAnalysisDTO roundAnalysisDTO : overallReportDTO.getRoundAnalysisDTOList()){
            int round_number = roundAnalysisDTO.getRoundNumber();

            // 保存accuracy_evaluation
            AccuracyEvaluationDTO accuracyEvaluationDTO = roundAnalysisDTO.getAccuracyEvaluationDTO();
            AccuracyEvaluationEntity accuracyEvaluationEntity = new AccuracyEvaluationEntity();
            accuracyEvaluationEntity.setReport_id(reportId);
            accuracyEvaluationEntity.setRound_number(round_number);
            accuracyEvaluationEntity.setCompleteness(accuracyEvaluationDTO.getCompleteness());
            accuracyEvaluationEntity.setLogicalRigor(accuracyEvaluationDTO.getLogicalRigor());
            accuracyEvaluationEntity.setMatchingDegree(accuracyEvaluationDTO.getMatchingDegree());
            accuracyEvaluationEntity.setTechnicalCorrectness(accuracyEvaluationDTO.getTechnical_correctness());
            accuracyEvaluationEntity.setScore(accuracyEvaluationDTO.getScore());
            accuracyEvaluationMapper.insert(accuracyEvaluationEntity);

            // 保存expression_analysis
            ExpressionAnalysisDTO expressionAnalysisDTO = roundAnalysisDTO.getExpressionAnalysisDTO();
            ExpressionAnalysisEntity expressionAnalysisEntity = new ExpressionAnalysisEntity();
            expressionAnalysisEntity.setReport_id(reportId);
            expressionAnalysisEntity.setRound_number(round_number);
            expressionAnalysisEntity.setRationalityAnalysis(expressionAnalysisDTO.getRationalityAnalysis());
            expressionAnalysisEntity.setDominantEmotion(expressionAnalysisDTO.getDominantEmotion());
            expressionAnalysisEntity.setIntensityDuration(expressionAnalysisDTO.getIntensityDuration());
            expressionAnalysisMapper.insert(expressionAnalysisEntity);

            InterviewProblemEntity interviewProblemEntity = new InterviewProblemEntity();
            interviewProblemEntity.setReportId(reportId);
            interviewProblemEntity.setRoundNumber(round_number);
            interviewProblemEntity.setQuestion(roundAnalysisDTO.getQuestion());
            interviewProblemEntity.setAnswer(roundAnalysisDTO.getAnswer());
            interviewProblemEntity.setAccuracyEvaluationId(accuracyEvaluationMapper.selectId(reportId, round_number));
            interviewProblemEntity.setExpressionAnalysisId(expressionAnalysisMapper.selectId(reportId, round_number));
            interviewProblemMapper.insert(interviewProblemEntity);

        }

        // 2.4 保存roundDetails
        for(RoundDetailDTO roundDetailDTO : overallReportDTO.getRoundDetailDTOList()){
            int round_number = roundDetailDTO.getRoundNumber();
            ExpressionInfoDTO expressionInfoDTO = roundDetailDTO.getExpressionInfoDTO();
            ExpressionInfoEntity expressionInfoEntity = new ExpressionInfoEntity();
            expressionInfoEntity.setReport_id(reportId);
            expressionInfoEntity.setRound_number(round_number);
            expressionInfoEntity.setConcentration(expressionInfoDTO.getConcentration());
            expressionInfoEntity.setConfidence(expressionInfoDTO.getConfidence());
            expressionInfoEntity.setDoubt(expressionInfoDTO.getDoubt());
            expressionInfoEntity.setHappy(expressionInfoDTO.getHappy());
            expressionInfoEntity.setNervous(expressionInfoDTO.getNervous());
            expressionInfoMapper.insert(expressionInfoEntity);

            IntegratedScoreDTO integratedScoreDTO = roundDetailDTO.getIntegratedScoreDTO();
            IntegratedScoreEntity integratedScoreEntity = new IntegratedScoreEntity();
            integratedScoreEntity.setReport_id(reportId);
            integratedScoreEntity.setRound_number(round_number);
            integratedScoreEntity.setExpression(integratedScoreDTO.getExpression());
            integratedScoreEntity.setLogic(integratedScoreDTO.getLogic());
            integratedScoreEntity.setMajor(integratedScoreDTO.getMajor());
            integratedScoreEntity.setStable(integratedScoreDTO.getStable());
            integratedScoreEntity.setStrain(integratedScoreDTO.getStrain());
            integratedScoreMapper.insert(integratedScoreEntity);
        }

        // 2.5 保存overallIntegratedScore
        OverallIntegratedScoreDTO overallIntegratedScoreDTO = overallReportDTO.getOverallIntegratedScoreDTO();
        OverallIntegratedScoreEntity overallIntegratedScoreEntity = new OverallIntegratedScoreEntity();
        overallIntegratedScoreEntity.setReport_id(reportId);
        overallIntegratedScoreEntity.setMajor(overallIntegratedScoreDTO.getMajor());
        overallIntegratedScoreEntity.setStable(overallIntegratedScoreDTO.getStable());
        overallIntegratedScoreEntity.setStrain(overallIntegratedScoreDTO.getStrain());
        overallIntegratedScoreEntity.setExpression(overallIntegratedScoreDTO.getExpression());
        overallIntegratedScoreEntity.setLogic(overallIntegratedScoreDTO.getLogic());
        overallIntegratedScoreMapper.insert(overallIntegratedScoreEntity);



        // 2.6 overallExpressionInfo
        OverallExpressionInfoDTO overallExpressionInfoDTO = overallReportDTO.getOverallExpressionInfoDTO();
        OverallExpressionInfoEntity overallExpressionInfoEntity = new OverallExpressionInfoEntity();
        overallExpressionInfoEntity.setReport_id(reportId);
        overallExpressionInfoEntity.setConcentration(overallExpressionInfoDTO.getConcentration());
        overallExpressionInfoEntity.setNervousness(overallExpressionInfoDTO.getNervousness());
        overallExpressionInfoEntity.setHappiness(overallExpressionInfoDTO.getHappiness());
        overallExpressionInfoEntity.setDoubtfulness(overallExpressionInfoDTO.getDoubtfulness());
        overallExpressionInfoEntity.setAdaptability(overallExpressionInfoDTO.getAdaptability());
        overallExpressionInfoEntity.setConfidence(overallExpressionInfoDTO.getConfidence());
        overallExpressionInfoEntity.setEmotionalStability(overallExpressionInfoDTO.getEmotionalStability());
        overallExpressionInfoMapper.insert(overallExpressionInfoEntity);


        // 3. 更新用户面试次数和平均分数
        updateUserInterviewStats(userId);

        return reportId;
    }

    /**
     * 更新用户的面试次数和平均分数
     */
    private void updateUserInterviewStats(Integer userId) {
        // 3.1 查询用户当前信息
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在，无法更新面试信息");
        }

        // 3.2 面试次数+1
        int newInterviewCount = user.getInterviewCount() == null ? 1 : user.getInterviewCount() + 1;
        user.setInterviewCount(newInterviewCount);

        // 3.3 查询用户所有历史面试分数
        List<Float> scores = interviewReportMapper.selectScoresByUserId(userId);

        // 3.4 计算平均分（总分/面试次数）
        float totalScore = 0;
        for (Float score : scores) {
            totalScore += score;
        }
        float avgScore = newInterviewCount > 0 ? totalScore / newInterviewCount : 0;
        user.setComprehensiveScore(avgScore); // 平均分数存入综合评分字段

        // 3.5 保存更新后的用户信息
        userMapper.updateInterviewStats(user);
    }


    @Override
    public InterviewReportListDTO getReportListByUsername(PageQueryDTO queryDTO) {
        // 1. 根据用户名查询用户
        UserEntity user = userMapper.selectByUsername(queryDTO.getUsername());
        if (user == null) {
            // 用户不存在，返回空列表
            InterviewReportListDTO result = new InterviewReportListDTO();
            result.setReports(new ArrayList<>());
            result.setTotal(0);
            result.setPageNum(queryDTO.getPageNum());
            result.setPageSize(queryDTO.getPageSize());
            return result;
        }

        // 2. 计算分页参数
        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        int limit = queryDTO.getPageSize();

        // 3. 查询分页数据
        List<InterviewReportEntity> reportEntities = interviewReportMapper.selectByUserId(user.getId(), offset, limit);

        // 4. 查询总记录数
        int total = interviewReportMapper.countByUserId(user.getId());

        // 5. 转换为DTO
        List<InterviewReportItemDTO> reportDTOs = reportEntities.stream()
                .map(entity -> {
                    InterviewReportItemDTO dto = new InterviewReportItemDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 6. 组装结果
        InterviewReportListDTO result = new InterviewReportListDTO();
        result.setReports(reportDTOs);
        result.setTotal(total);
        result.setPageNum(queryDTO.getPageNum());
        result.setPageSize(queryDTO.getPageSize());

        return result;
    }


    @Override
    public InterviewReportDTO getReportById(Integer reportId) {
        // 1. 查询主报告实体
        InterviewReportEntity reportEntity = interviewReportMapper.selectById(reportId);
        if (reportEntity == null) {
            return null;
        }

        // 2. 构建返回DTO（简化逻辑，实际需完善字段映射）
        InterviewReportDTO reportDTO = new InterviewReportDTO();
        reportDTO.setUsername(BaseContext.getCurrentUsername());
        reportDTO.setInterviewField(reportEntity.getInterviewField());
        reportDTO.setInterviewPosition(reportEntity.getInterviewPosition());
        reportDTO.setInterviewPositionKeyword(reportEntity.getInterviewPositionKeyword());
        reportDTO.setInterviewScore(reportEntity.getInterviewScore());
        reportDTO.setTotalInterviewTime(reportEntity.getTotalInterviewTime());



        // 假设数据库返回的日期时间字符串格式为 "yyyy-MM-dd HH:mm:ss"
        String beginTimeStr = reportEntity.getBeginTime(); // 获取字符串类型的时间

        if (beginTimeStr != null && !beginTimeStr.isEmpty()) {
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            // 解析字符串为 LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(beginTimeStr, formatter);
            // 设置到 DTO 中
            reportDTO.setBeginTime(localDateTime);
        }

        // 3. 填充其他DTO字段（整体报告、轮次分析等）
        OverallReportDTO overallReportDTO = new OverallReportDTO();

        // keyIssuseDTO
        KeyIssuesDTO keyIssuesDTO = new KeyIssuesDTO();
        List<TopPerformanceDTO> topPerformanceDTOList = topPerformanceMapper.getTopPerformanceDTOList(reportId);
        List<WeakPerformanceDTO> weakPerformanceDTOList = weakPerformanceMapper.getWeakPerformanceDTOList(reportId);
        keyIssuesDTO.setTopPerformanceDTOList(topPerformanceDTOList);
        keyIssuesDTO.setWeakPerformanceDTOList(weakPerformanceDTOList);
        overallReportDTO.setKeyIssuesDTO(keyIssuesDTO);

        // overallReportDTO.summaryDTO
        InterviewSummaryEntity interviewSummaryEntity = interviewSummaryMapper.selectByReportId(reportId);
        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setPerformanceOverview(interviewSummaryEntity.getPerformanceOverview());
        summaryDTO.setExpressionPattern(interviewSummaryEntity.getExpressionPattern());
        summaryDTO.setAverageAccuracyScore(interviewSummaryEntity.getAverageAccuracyScore());
        overallReportDTO.setSummaryDTO(summaryDTO);


        // round_analysis
        List<RoundAnalysisDTO> roundAnalysisDTOList = new ArrayList<>();
        //roundDetails
        List<RoundDetailDTO> roundDetailDTOList = new ArrayList<>();

        List<InterviewProblemEntity> interviewProblemEntities = interviewProblemMapper.selectByReportId(reportId);
        for(InterviewProblemEntity interviewProblemEntity : interviewProblemEntities) {
            int round_number = interviewProblemEntity.getRoundNumber();
            RoundAnalysisDTO roundAnalysisDTO = new RoundAnalysisDTO();
            roundAnalysisDTO.setQuestion(interviewProblemEntity.getQuestion());
            roundAnalysisDTO.setAnswer(interviewProblemEntity.getAnswer());
            roundAnalysisDTO.setRoundNumber(round_number);

            AccuracyEvaluationDTO accuracyEvaluationDTO = accuracyEvaluationMapper.getAccuracyEvaluationDTO(reportId,round_number);
            ExpressionAnalysisDTO expressionAnalysisDTO = expressionAnalysisMapper.getExpressionAnalysisDTO(reportId, round_number);
            roundAnalysisDTO.setAccuracyEvaluationDTO(accuracyEvaluationDTO);
            roundAnalysisDTO.setExpressionAnalysisDTO(expressionAnalysisDTO);
            roundAnalysisDTOList.add(roundAnalysisDTO);


            RoundDetailDTO roundDetailDTO = new RoundDetailDTO();
            roundDetailDTO.setRoundNumber(round_number);
            roundDetailDTO.setExpressionInfoDTO(expressionInfoMapper.getExpressionInfoDTO(reportId, round_number));
            roundDetailDTO.setIntegratedScoreDTO(integratedScoreMapper.getIntegratedScoreDTO(reportId, round_number));
            roundDetailDTOList.add(roundDetailDTO);
        }


        overallReportDTO.setRoundAnalysisDTOList(roundAnalysisDTOList);
        overallReportDTO.setRoundDetailDTOList(roundDetailDTOList);


        OverallIntegratedScoreEntity overallIntegratedScoreEntity = overallIntegratedScoreMapper.selectByReportId(reportId);
        OverallIntegratedScoreDTO overallIntegratedScoreDTO = new OverallIntegratedScoreDTO();
        overallIntegratedScoreDTO.setExpression(overallIntegratedScoreEntity.getExpression());
        overallIntegratedScoreDTO.setStrain(overallIntegratedScoreEntity.getStrain());
        overallIntegratedScoreDTO.setMajor(overallIntegratedScoreEntity.getMajor());
        overallIntegratedScoreDTO.setLogic(overallIntegratedScoreEntity.getLogic());
        overallIntegratedScoreDTO.setStable(overallIntegratedScoreEntity.getStable());
        overallReportDTO.setOverallIntegratedScoreDTO(overallIntegratedScoreDTO);

        OverallExpressionInfoEntity overallExpressionInfoEntity = overallExpressionInfoMapper.selectByReportId(reportId);
        OverallExpressionInfoDTO overallExpressionInfoDTO = new OverallExpressionInfoDTO();
        overallExpressionInfoDTO.setAdaptability(overallExpressionInfoEntity.getAdaptability());
        overallExpressionInfoDTO.setHappiness(overallExpressionInfoEntity.getHappiness());
        overallExpressionInfoDTO.setDoubtfulness(overallExpressionInfoEntity.getDoubtfulness());
        overallExpressionInfoDTO.setConfidence(overallExpressionInfoEntity.getConfidence());
        overallExpressionInfoDTO.setNervousness(overallExpressionInfoEntity.getNervousness());
        overallExpressionInfoDTO.setConcentration(overallExpressionInfoEntity.getConcentration());
        overallExpressionInfoDTO.setEmotionalStability(overallExpressionInfoEntity.getEmotionalStability());
        overallReportDTO.setOverallExpressionInfoDTO(overallExpressionInfoDTO);




        reportDTO.setOverallReportDTO(overallReportDTO);

        return reportDTO;
    }

    // 辅助方法：根据轮次号查找对应的轮次详情
    private RoundDetailDTO findRoundDetail(List<RoundDetailDTO> detailList, String roundNumber) {
        if (detailList == null || roundNumber == null) {
            return null;
        }
        for (RoundDetailDTO detail : detailList) {
            if (String.valueOf(detail.getRoundNumber()).equals(roundNumber)) {
                return detail;
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void deleteReportById(Integer reportId) {
        // 1. 删除面试问题相关数据
        interviewProblemMapper.deleteByReportId(reportId);

        // 2. 删除表情信息相关数据
        expressionInfoMapper.deleteByReportId(reportId);

        // 3. 删除综合评分相关数据
        integratedScoreMapper.deleteByReportId(reportId);

        // 4. 删除优势表现相关数据
        topPerformanceMapper.deleteByReportId(reportId);

        // 5. 删除弱项表现相关数据
        weakPerformanceMapper.deleteByReportId(reportId);

        // 6. 删除面试总结相关数据
        interviewSummaryMapper.deleteByReportId(reportId);

        // 7. 删除准确率评估相关数据
        accuracyEvaluationMapper.deleteByReportId(reportId);

        // 8. 删除表情分析相关数据
        expressionAnalysisMapper.deleteByReportId(reportId);

        // 9. 删除核心评估数据
        overallIntegratedScoreMapper.deleteByReportId(reportId);

        // 10. 删除综合评估数据
        overallExpressionInfoMapper.deleteByReportId(reportId);

        // 11. 最后删除主报告数据
        interviewReportMapper.deleteReportById(reportId);


    }





}