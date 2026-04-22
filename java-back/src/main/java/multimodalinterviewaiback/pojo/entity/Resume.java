package multimodalinterviewaiback.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import multimodalinterviewaiback.pojo.entity.*;

import java.util.List;

@Data
public class Resume {
    @JsonProperty("personal_info")
    private PersonalInfo personalInfo;

    @JsonProperty("job_intention")
    private JobIntention jobIntention;

    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    private Skills skills;
    private Honors honors;

    @JsonProperty("self_evaluation")
    private String selfEvaluation;

    private String hobbies;
}