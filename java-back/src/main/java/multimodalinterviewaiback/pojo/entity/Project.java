package multimodalinterviewaiback.pojo.entity;// Project.java
import lombok.Data;

@Data
public class Project {
    private Long id;
    private Long personalId;
    private String name;
    private String period;
    private String role;
    private String description;
    private String responsibilities;
    private String achievements;
}