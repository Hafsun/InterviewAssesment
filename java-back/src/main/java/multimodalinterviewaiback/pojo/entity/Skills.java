package multimodalinterviewaiback.pojo.entity;// Skills.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Skills {
    private Long id;
    private Long personalId;

    @JsonProperty("professional_skills")
    private String professionalSkills;

    private String languages;

    @JsonProperty("other_skills")
    private String otherSkills;
}