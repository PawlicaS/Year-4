package ie.szymon.repo;

import ie.szymon.entities.School;

import java.util.List;

public interface SchoolRepo {
    int count();
    List<School> getAll();
    School findById(int id);
    boolean exists(int id);
    boolean existsByName(String name);
    int deleteSchool(int id);
    int createSchool(School newSchool);
    int changeName(int id, String newName);
}
