package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeUpdateDTO {
    private Integer id;
    private String name;
    private String tags;
}
