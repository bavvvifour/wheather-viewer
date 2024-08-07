package pet.service;

import pet.dao.LocationDao;
import pet.dao.impl.LocationDaoImpl;
import pet.model.Location;

import java.util.List;

public class LocationService {
    private final LocationDao locationDao = new LocationDaoImpl();

    public void saveLocation(Location location) {
        locationDao.saveLocation(location);
    }
    public void deleteLocation(int id) {
        locationDao.deleteLocation(id);
    }
    public Location findByName(String name) {
        return locationDao.findByName(name);
    }
    public List<Location> findAllLocations(int userId) {
        return locationDao.findAllLocations(userId);
    }

}
