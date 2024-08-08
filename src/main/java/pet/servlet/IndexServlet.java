package pet.servlet;

import pet.dto.OpenWeatherMapDto;
import pet.exception.location.LocationNotFoundException;
import pet.model.Location;
import pet.model.User;
import pet.service.LocationService;
import pet.service.UserService;
import pet.service.WeatherApiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class IndexServlet extends BaseServlet {
    private final WeatherApiService weatherApiService = new WeatherApiService();
    private final LocationService locationService = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locationName = req.getParameter("name");
        if (locationName != null && !locationName.trim().isEmpty()) {
            List<OpenWeatherMapDto> location = weatherApiService.searchCitiesByName(locationName);
            context.setVariable("cities", location);
        }

        String successMessage = (String) req.getSession().getAttribute("success");
        if (successMessage != null) {
            context.setVariable("success", successMessage);
            req.getSession().removeAttribute("success");
        }

        String errorMessage = (String) req.getSession().getAttribute("locationExists");
        if (errorMessage != null) {
            context.setVariable("locationExists", errorMessage);
            req.getSession().removeAttribute("locationExists");
        }

        context.setVariable("user", req.getSession().getAttribute("user"));
        templateEngine.process("index", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locationName = req.getParameter("nameLocation");

        String latitudeStr = req.getParameter("latitude");
        String longitudeStr = req.getParameter("longitude");

        if (locationName != null) {
            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);

            String userName = (String) req.getSession().getAttribute("user");

            UserService userService = new UserService();
            User user = userService.findByLogin(userName);

            Location existingLocation;
            try {
                existingLocation = locationService.findByName(locationName);
            } catch (LocationNotFoundException e) {
                existingLocation = null;
            }

            if (existingLocation != null) {
                req.getSession().setAttribute("locationExists", "Location " + locationName + " already exists");
            } else {
                Location location = new Location();

                location.setName(locationName);
                location.setUser(user);
                location.setLongitude(longitude);
                location.setLatitude(latitude);

                locationService.saveLocation(location);

                req.getSession().setAttribute("success", "Location " + locationName + " added successfully.");
            }
            resp.sendRedirect("/");
        }
    }
}