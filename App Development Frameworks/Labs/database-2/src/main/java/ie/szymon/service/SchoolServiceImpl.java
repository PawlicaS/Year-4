package ie.szymon.service;

import ie.szymon.entities.School;
import ie.szymon.repo.SchoolRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    SchoolRepo schoolRepo;

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<School> findAll() {
        return schoolRepo.getAll();
    }

    @Override
    public School findASchool(int id) {
        if (schoolRepo.exists(id))
            return schoolRepo.findById(id);
        log.error("Could not find school with id " + id + " because it does not exist");
        return schoolRepo.findById(1);
    }

    @Override
    public boolean deleteSchool(int id) {
        if (schoolRepo.exists(id)) {
            return schoolRepo.deleteSchool(id) == 1;
        }
        log.error("Could not delete school with id " + id + " because it does not exist");
        return false;
    }

    @Override
    public boolean addSchool(School newSchool) {
        if (schoolRepo.existsByName(newSchool.getName())) {
            log.error("Could not add school because a school called " + newSchool.getName() + " already exists");
            return false;
        }
        if (schoolRepo.exists(newSchool.getSchoolId())) {
            log.error("Could not add school because a school with id " + newSchool.getSchoolId() + " already exists");
            return false;
        }
        return schoolRepo.createSchool(newSchool) == 1;
    }

    @Override
    public boolean changeName(int id, String newName) {
        if (schoolRepo.existsByName(newName)) {
            log.error("Could not add school because a school called " + newName + " already exists");
            return false;
        }
        if (! schoolRepo.exists(id)) {
            log.error("Could not add school because a school with id " + id + " does not exist");
            return false;
        }
        return schoolRepo.changeName(id, newName) == 1;
    }
}
