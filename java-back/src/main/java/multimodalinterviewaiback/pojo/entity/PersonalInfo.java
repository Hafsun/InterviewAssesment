package multimodalinterviewaiback.pojo.entity;// PersonalInfo.java
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonalInfo {
    private String username;

    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String phone;
    private String email;

    @JsonProperty("current_location")
    private String currentLocation;

    @JsonProperty("self_evaluation")
    private String selfEvaluation;

    private String hobbies;
    private Date createTime;
    private Date updateTime;
}