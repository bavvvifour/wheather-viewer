package pet.util;

import pet.dto.OpenWeatherMapDto;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class WeatherIconSelectorUtil {
    private final Map<String, String> dayIcons = new HashMap<>();
    private final Map<String, String> nightIcons = new HashMap<>();

    public WeatherIconSelectorUtil() {
        dayIcons.put("clear", "icons/clear_sky/01d.svg");
        dayIcons.put("clouds", "icons/few_clouds/02d.svg");
        dayIcons.put("scattered_clouds", "icons/scattered_clouds/03d.svg");
        dayIcons.put("broken_clouds", "icons/broken_clouds/04d.svg");
        dayIcons.put("shower_rain", "icons/shower_rain/09d.svg");
        dayIcons.put("rain", "icons/rain/10d.svg");
        dayIcons.put("thunderstorm", "icons/thunderstorm/11d.svg");
        dayIcons.put("snow", "icons/snow/13d.svg");
        dayIcons.put("mist", "icons/mist/50d.svg");
        dayIcons.put("drizzle", "icons/notfound.svg");
        dayIcons.put("smoke", "icons/notfound.svg");
        dayIcons.put("haze", "icons/notfound.svg");
        dayIcons.put("dust", "icons/notfound.svg");
        dayIcons.put("fog", "icons/notfound.svg");
        dayIcons.put("sand", "icons/notfound.svg");
        dayIcons.put("ash", "icons/notfound.svg");
        dayIcons.put("squall", "icons/notfound.svg");
        dayIcons.put("tornado", "icons/notfound.svg");


        nightIcons.put("clear", "icons/clear_sky/01n.svg");
        nightIcons.put("clouds", "icons/few_clouds/02n.svg");
        nightIcons.put("scattered_clouds", "icons/scattered_clouds/03n.svg");
        nightIcons.put("broken_clouds", "icons/broken_clouds/04n.svg");
        nightIcons.put("shower_rain", "icons/shower_rain/09n.svg");
        nightIcons.put("rain", "icons/rain/10n.svg");
        nightIcons.put("thunderstorm ", "icons/thunderstorm/11n.svg");
        nightIcons.put("snow", "icons/snow/13n.svg");
        nightIcons.put("mist", "icons/mist/50n.svg");
        nightIcons.put("drizzle", "icons/notfound.svg");
        nightIcons.put("smoke", "icons/notfound.svg");
        nightIcons.put("haze", "icons/notfound.svg");
        nightIcons.put("dust", "icons/notfound.svg");
        nightIcons.put("fog", "icons/notfound.svg");
        nightIcons.put("sand", "icons/notfound.svg");
        nightIcons.put("ash", "icons/notfound.svg");
        nightIcons.put("squall", "icons/notfound.svg");
        nightIcons.put("tornado", "icons/notfound.svg");
    }

    public String getIconForWeather(OpenWeatherMapDto openWeatherMapDto) {
        long unixTime = openWeatherMapDto.getData();
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(openWeatherMapDto.getTimezone()));
        ZonedDateTime localTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(unixTime), zoneId);

        boolean isNight = isNightTime(localTime);

        String weatherMain = openWeatherMapDto.getWeatherDto().get(0).getParam().toLowerCase();

        return isNight ? nightIcons.get(weatherMain) : dayIcons.get(weatherMain);
    }

    private boolean isNightTime(ZonedDateTime localTime) {
        LocalTime time = localTime.toLocalTime();
        return time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(18, 0));
    }
}
