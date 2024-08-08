package pet.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinationsDto {
    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;
}
