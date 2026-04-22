package multimodalinterviewaiback.mapper;// PersonalInfoMapper.java
import multimodalinterviewaiback.pojo.entity.PersonalInfo;
import multimodalinterviewaiback.pojo.vo.ResumeSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonalInfoMapper {
    void insert(PersonalInfo personalInfo);
    void deleteById(Long id);
    PersonalInfo selectById(Long id);

    // 新增分页查询方法：查询简历基本信息
    List<ResumeSimpleVO> selectSimpleInfoByPage(
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize,
            @Param("username") String username
    );

    // 新增：查询总记录数（用于分页计算）
    Integer selectTotalCount();
}
