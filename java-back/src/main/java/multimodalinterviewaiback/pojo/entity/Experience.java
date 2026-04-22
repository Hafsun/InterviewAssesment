package multimodalinterviewaiback.pojo.entity;// Experience.java
import lombok.Data;

@Data
public class Experience {
    private Long id;
    private Long personalId;
    private String company;
    private String period;
    private String position;
    private String department;
    private String responsibilities;
    private String achievements;
}