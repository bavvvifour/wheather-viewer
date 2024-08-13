package pet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pet.dto.entity.CoordinationsDto;
import pet.dto.entity.MainDto;
import pet.dto.entity.SysDto;
import pet.dto.entity.WeatherDto;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapDto {
    @JsonProperty("coord")
    private CoordinationsDto coordinationsDto;

    @JsonProperty("weather")
    private List<WeatherDto> weatherDto;

    @JsonProperty("main")
    private MainDto mainDto;

    @JsonProperty("sys")
    private SysDto sysDto;

    @JsonProperty("name")
    private String name;

    @JsonProperty("dt")
    private long data;

    @JsonProperty("timezone")
    private int timezone;
}
