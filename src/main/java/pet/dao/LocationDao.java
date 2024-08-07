package pet.dao;

import pet.exception.location.LocationAlreadyExistsException;
import pet.exception.location.LocationDeleteException;
import pet.exception.location.LocationNotFoundException;
import pet.exception.location.FindAllLocationsException;
import pet.model.Location;

import java.util.List;

public interface LocationDao {
    void saveLocation(Location location) throws LocationAlreadyExistsException;
    void deleteLocation(int id) throws LocationDeleteException;
    Location findByName(String name) throws LocationNotFoundException;
    List<Location> findAllLocations(int userId) throws FindAllLocationsException;
}
