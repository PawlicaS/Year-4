package ie.szymon.repo;

import ie.szymon.entities.Director;

import java.util.List;

public interface DirectorRepo {
    List<Director> getAll();
    boolean exists(int directorId);
    boolean existsByName(String firstName, String lastName);
    int createDirector(Director newDirector);
    int deleteDirector(int directorId);
    int changeActive(int directorId, int newActive);
    int findInactiveDirectors();
}
