package multimodalinterviewaiback.pojo.entity;// Education.java
import lombok.Data;

@Data
public class Education {
    private Long id;
    private Long personalId;
    private String school;
    private String period;
    private String degree;
    private String major;
    private String gpa;
    private String courses;
}