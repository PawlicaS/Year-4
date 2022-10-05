package ie.szymon.service;

import ie.szymon.entities.Director;

import java.util.List;

public interface DirectorService {
    List<Director> listAll();
    boolean addDirector(Director newDirector);
    boolean deleteDirector(int directorId);
    boolean changeActive(int directorId, int newActive);
    int inactiveDirectors();
}
