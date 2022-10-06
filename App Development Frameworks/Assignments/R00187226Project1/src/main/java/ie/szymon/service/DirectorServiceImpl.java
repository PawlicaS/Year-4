package ie.szymon.service;

import ie.szymon.entities.Director;
import ie.szymon.repo.DirectorRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DirectorServiceImpl implements DirectorService {
    @Autowired
    DirectorRepo directorRepo;

    @Override
    public List<Director> listAll() {
        return directorRepo.getAll();
    }

    @Override
    public boolean addDirector(Director newDirector) {
        if (directorRepo.exists(newDirector.getDirectorId())) {
            log.error("Could not add director because a director with id " + newDirector.getDirectorId() + " already exists");
            return false;
        }
        if (directorRepo.existsByName(newDirector.getFirstName(), newDirector.getLastName())) {
            log.error("Could not add director because a director called " + newDirector.getFirstName() + " " + newDirector.getLastName() + " already exists");
            return false;
        }
        return directorRepo.createDirector(newDirector) == 1;
    }

    @Override
    public boolean deleteDirector(int directorId) {
        if (directorRepo.exists(directorId))
            return directorRepo.deleteDirector(directorId) == 1;
        log.error("Could not delete director with id " + directorId + " because it does not exist");
        return false;
    }

    @Override
    public boolean changeActive(int directorId, int newActive) {
        if (! directorRepo.exists(directorId)) {
            log.error("Could not change active because a director with id " + directorId + " does not exist");
            return false;
        }
        if (! (newActive == 0 || newActive == 1)) {
            log.error("Could not change active because " + newActive + " is not a valid value (0, 1)");
            return false;
        }
        return directorRepo.changeActive(directorId, newActive) == 1;
    }

    @Override
    public int inactiveDirectors() {
        return directorRepo.findInactiveDirectors();
    }
}
