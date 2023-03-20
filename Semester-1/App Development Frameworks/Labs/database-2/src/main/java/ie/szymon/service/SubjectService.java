package ie.szymon.service;

import ie.szymon.entities.Result;
import ie.szymon.entities.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    boolean moveSchool(int subjectId, int newSchoolId);
    Optional<List<Subject>> findSubjectsInSchool(int schoolId);
    Optional<Result> findSubjectAndSchool(int subjectId);
    List<Result> findAll();
    boolean delete(int subjectId);
}
