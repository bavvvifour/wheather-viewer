package pet.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dao.LocationDao;
import pet.exception.location.LocationAlreadyExistsException;
import pet.exception.location.LocationDeleteException;
import pet.exception.location.LocationNotFoundException;
import pet.exception.location.FindAllLocationsException;
import pet.model.Location;
import pet.util.PersistenceUtil;

import java.util.List;

public class LocationDaoImpl implements LocationDao {
    private static final Logger logger = LoggerFactory.getLogger(LocationDaoImpl.class);

    @Override
    public void saveLocation(Location location) throws LocationAlreadyExistsException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(location);
            transaction.commit();
            logger.info("Location {} saved successfully", location.getName());
        } catch (Exception e) {
            if (transaction != null) {
                logger.error("Failed to save location {}, location already exists", location.getName(), e);
                transaction.rollback();
            }
            throw new LocationAlreadyExistsException("Location " + location.getName() + " already exists", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteLocation(int id) throws LocationDeleteException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Location location = entityManager.find(Location.class, id);
            if (location != null) {
                entityManager.remove(location);
                transaction.commit();
                logger.info("Location {} delete successfully", location.getName());
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                logger.error("Failed to delete location", e);
                transaction.rollback();
            }
            throw new LocationDeleteException("Failed to delete location", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Location findByName(String name) throws LocationNotFoundException {
        try (EntityManager entityManager = PersistenceUtil.getEntityManager()) {
            return entityManager.createQuery("SELECT l FROM Location l WHERE l.name = :name", Location.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            logger.warn("Location with name {} not found", name);
            throw new LocationNotFoundException("Location " + name + " not found", e);
        }
    }

    @Override
    public List<Location> findAllLocations(int userId) throws FindAllLocationsException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT l FROM Location l WHERE l.user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Failed to find locations for user ID {}", userId, e);
            throw new FindAllLocationsException("Failed to find locations", e);
        } finally {
            entityManager.close();
        }
    }
}
