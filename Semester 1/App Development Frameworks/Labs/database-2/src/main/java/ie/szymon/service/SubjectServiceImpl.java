package ie.szymon.service;

import ie.szymon.entities.Result;
import ie.szymon.entities.Subject;
import ie.szymon.repo.SchoolRepo;
import ie.szymon.repo.SubjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    SchoolRepo schoolRepo;

    @Override
    public boolean moveSchool(int subjectId, int newSchoolId) {
        if (!subjectRepo.exists(subjectId) || ! schoolRepo.exists(newSchoolId))
            return false;
        return subjectRepo.moveSchool(subjectId, newSchoolId) == 1;
    }

    @Override
    public Optional<List<Subject>> findSubjectsInSchool(int schoolId) {
        if (schoolRepo.exists(schoolId)) {
            List<Subject> subjects = subjectRepo.findSubjectsInSchool(schoolId);
            return subjects.isEmpty()? Optional.empty(): Optional.of(subjects);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Result> findSubjectAndSchool(int subjectId) {
        return subjectRepo.exists(subjectId)?
                Optional.of(subjectRepo.findSubjectNameAndSchool(subjectId))
                : Optional.empty();
    }

    @Override
    public List<Result> findAll() {
        return subjectRepo.findAll();
    }

    @Override
    public boolean delete(int subjectId) {
        if (subjectRepo.exists(subjectId))
            return subjectRepo.delete(subjectId) == 1;
        return false;
    }
}
