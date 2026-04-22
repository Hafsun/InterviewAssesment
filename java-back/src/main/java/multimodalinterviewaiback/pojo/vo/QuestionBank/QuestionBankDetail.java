package multimodalinterviewaiback.pojo.vo.QuestionBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBankDetail {

    private Integer id;
    private String question;
    private String answer;
    private Integer overviewId;

}
