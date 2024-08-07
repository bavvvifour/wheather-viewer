package pet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dto.WheatherDto;
import pet.exception.api.WeatherServiceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherApiService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiService.class);

    private final static String API_ID = "SECRET";
    private final static String BASE_API_URL = "https://api.openweathermap.org";
    private final static String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";
    private final static String UNITS = "metric";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static HttpClient httpClient = HttpClient.newHttpClient();

    public WheatherDto getWheatherByName(String name) throws WeatherServiceException {
        try {
            HttpRequest request = buildRequest(buildUriForCurrentWheatherByName(name));
            HttpResponse<String> response = getResponse(request);

            if (response.statusCode() == 404) {
                throw new WeatherServiceException("Location " + name + " not found");
            }
            return objectMapper.readValue(response.body(), WheatherDto.class);
        } catch (WeatherServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {}", name, e);
            throw new WeatherServiceException("Error fetching weather data", e);
        }
    }

    private static HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    private static HttpResponse<String> getResponse(HttpRequest httpRequest) throws IOException, InterruptedException {
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private static URI buildUriForCurrentWheatherByName(String name) {
        return URI.create(
                BASE_API_URL
                        + WEATHER_API_URL_SUFFIX
                        + "?q="
                        + name
                        + "&units="
                        + UNITS
                        + "&appid="
                        + API_ID
        );
    }

}
