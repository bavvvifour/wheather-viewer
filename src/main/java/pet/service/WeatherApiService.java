package pet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dto.OpenWeatherMapDto;
import pet.exception.api.WeatherServiceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class WeatherApiService {
    private final static Dotenv dotenv = Dotenv .load();

    private final static Logger logger = LoggerFactory.getLogger(WeatherApiService.class);
    private final static String API_ID = dotenv.get("API_ID");
    private final static String BASE_API_URL = "https://api.openweathermap.org";
    private final static String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";
    private final static String FIND_API_URL_SUFFIX = "/data/2.5/find";
    private final static String UNITS = "metric";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static HttpClient httpClient = HttpClient.newHttpClient();

    public OpenWeatherMapDto getWeatherByCoordinates(double latitude, double longitude) throws WeatherServiceException {
        try {
            HttpRequest request = buildRequest(buildUriForCurrentWeatherByCoordinates(latitude, longitude));
            HttpResponse<String> response = getResponse(request);

            if (response.statusCode() == 404) {
                throw new WeatherServiceException("Location with coordinates (" + latitude + ", " + longitude + ") not found");
            }
            return objectMapper.readValue(response.body(), OpenWeatherMapDto.class);
        } catch (WeatherServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching weather data for coordinates: ({}, {})", latitude, longitude, e);
            throw new WeatherServiceException("Error fetching weather data", e);
        }
    }

    public List<OpenWeatherMapDto> searchCitiesByName(String name) throws WeatherServiceException {
        try {
            HttpRequest request = buildRequest(buildUriForCitiesSearchByName(name));
            HttpResponse<String> response = getResponse(request);

            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode listNode = rootNode.path("list");

            if (listNode.isArray() && listNode.isEmpty()) {
                throw new WeatherServiceException("Location " + name + " not found");
            }

            List<OpenWeatherMapDto> cities = new ArrayList<>();
            Iterator<JsonNode> elements = listNode.elements();
            while (elements.hasNext()) {
                JsonNode cityNode = elements.next();
                OpenWeatherMapDto city = objectMapper.treeToValue(cityNode, OpenWeatherMapDto.class);
                cities.add(city);
            }
            return cities;
        } catch (WeatherServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
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

    private static URI buildUriForCitiesSearchByName(String name) {
        return URI.create(
                BASE_API_URL
                        + FIND_API_URL_SUFFIX
                        + "?q="
                        + name
                        + "&units="
                        + UNITS
                        + "&appid="
                        + API_ID
        );
    }
    
    private static URI buildUriForCurrentWeatherByCoordinates(double latitude, double longitude) {
        return URI.create(
                BASE_API_URL
                        + WEATHER_API_URL_SUFFIX
                        + "?lat="
                        + latitude
                        + "&lon="
                        + longitude
                        + "&units="
                        + UNITS
                        + "&appid="
                        + API_ID
        );
    }
}
