package multimodalinterviewaiback.pojo.vo.QuestionBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 模块文件分组VO：对应前端需要的「模块-文件列表」结构
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleFileGroupVO {
    // 模块ID（和前端moduleList中的id对应）
    private Integer moduleId;
    // 该模块下的所有文件列表（用现有的QuestionBankVO封装单个文件）
    private List<QuestionBankVO> files;
}