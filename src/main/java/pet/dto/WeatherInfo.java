package pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pet.dto.OpenWeatherMapDto;

@Getter
@AllArgsConstructor
public class WeatherInfo {
    private OpenWeatherMapDto weatherDto;
    private String iconPath;
}
