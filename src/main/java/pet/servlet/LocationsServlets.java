package pet.servlet;

import pet.model.Location;
import pet.model.User;
import pet.service.LocationService;
import pet.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/locations")
public class LocationsServlets extends BaseServlet {
    private final LocationService locationService = new LocationService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = (String) req.getSession().getAttribute("user");
        User user = userService.findByLogin(userName);

        List<Location> locations = locationService.findAllLocations(user.getId());
        context.setVariable("locations", locations);

        templateEngine.process("locations", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int locationId = Integer.parseInt(req.getParameter("locationId"));
            locationService.deleteLocation(locationId);
            resp.sendRedirect("/locations");
        }
    }
}
