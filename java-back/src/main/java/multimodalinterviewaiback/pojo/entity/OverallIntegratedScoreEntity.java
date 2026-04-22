package multimodalinterviewaiback.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverallIntegratedScoreEntity {
    private int id;
    private Integer report_id;

    private int major;
    private int expression;
    private int logic;
    private int strain;
    private int stable;
}
