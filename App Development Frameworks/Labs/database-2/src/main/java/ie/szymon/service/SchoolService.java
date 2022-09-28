package ie.szymon.service;

import ie.szymon.entities.School;

import java.util.List;

public interface SchoolService {
    int count();
    List<School> findAll();
    School findASchool(int id);
    boolean deleteSchool(int id);
    boolean addSchool(School newSchool);
    boolean changeName(int id, String newName);
}
