package ie.szymon.repo;

import ie.szymon.entities.Subject;
import ie.szymon.entities.Result;

import java.util.List;

public interface SubjectRepo {
    boolean exists(int subjectId);
    int delete(int subjectId);
    int moveSchool(int subjectId, int newSchoolId);
    List<Subject> findSubjectsInSchool(int schoolId);
    Result findSubjectNameAndSchool(int subjectId);
    List<Result> findAll();
}
