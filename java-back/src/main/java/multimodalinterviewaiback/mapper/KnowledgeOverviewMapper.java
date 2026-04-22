package multimodalinterviewaiback.mapper;

import multimodalinterviewaiback.pojo.dto.KnowledgeUpdateDTO;
import multimodalinterviewaiback.pojo.vo.Knowledge.KnowledgeVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KnowledgeOverviewMapper {

    @Select("select * from knowledge_overview")
    List<KnowledgeVO> selectAll();

    @Insert("insert into knowledge_overview (name, size, url, file_type, tags, upload_time, update_time, creator) " +
            "VALUES (#{name},#{size},#{url},#{fileType},#{tags},#{uploadTime},#{updateTime},#{creator})")
    void insert(KnowledgeVO knowledgeVO);

    @Update("update knowledge_overview set name = #{dto.name}, tags = #{dto.tags} where id = #{dto.id}")
    void updateNameAndTagsById(@Param("dto") KnowledgeUpdateDTO updateDTO);

    @Delete("delete from knowledge_overview where id = #{id}")
    void deleteById(Integer id);
}
