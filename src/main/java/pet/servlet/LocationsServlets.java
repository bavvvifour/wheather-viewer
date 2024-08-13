package pet.servlet;

import pet.dto.WeatherInfo;
import pet.dto.OpenWeatherMapDto;
import pet.model.Location;
import pet.model.User;
import pet.service.LocationService;
import pet.service.UserService;
import pet.service.WeatherApiService;
import pet.util.WeatherIconSelectorUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/locations")
public class LocationsServlets extends BaseServlet {
    private final LocationService locationService = new LocationService();
    private final UserService userService = new UserService();
    private final WeatherApiService weatherApiService = new WeatherApiService();
    private final WeatherIconSelectorUtil weatherIconSelectorUtil = new WeatherIconSelectorUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = (String) req.getSession().getAttribute("user");
        User user = userService.findByLogin(userName);

        List<Location> locations = locationService.findAllLocations(user.getId());
        Map<Location, WeatherInfo> locationWeatherMap = new HashMap<>();

        for (Location location : locations) {
            OpenWeatherMapDto weather = weatherApiService.getWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            String iconPath = weatherIconSelectorUtil.getIconForWeather(weather);
            WeatherInfo weatherInfo = new WeatherInfo(weather, iconPath);
            locationWeatherMap.put(location, weatherInfo);
        }
        context.setVariable("locationWeatherMap", locationWeatherMap);
        templateEngine.process("locations", context, resp.getWriter());

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int locationId = Integer.parseInt(req.getParameter("locationId"));
            locationService.deleteLocation(locationId);
            resp.sendRedirect(req.getContextPath() + "/locations");
        }
    }
}