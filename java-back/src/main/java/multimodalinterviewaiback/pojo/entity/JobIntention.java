package multimodalinterviewaiback.pojo.entity;// JobIntention.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobIntention {
    private Long id;
    private Long personalId;
    private String position;

    @JsonProperty("expected_salary")
    private String expectedSalary;

    @JsonProperty("desired_city")
    private String desiredCity;

    @JsonProperty("available_time")
    private String availableTime;
}