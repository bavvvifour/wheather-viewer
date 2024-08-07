package pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WheatherDto {
    @JsonProperty("main")
    private WhetherMainDto whetherMainDto;

    @JsonProperty("name")
    private String name;
}
