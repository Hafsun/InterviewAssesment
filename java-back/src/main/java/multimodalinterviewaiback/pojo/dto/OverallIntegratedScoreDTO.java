package multimodalinterviewaiback.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverallIntegratedScoreDTO {
    private int major;
    private int expression;
    private int logic;
    private int strain;
    private int stable;
}
