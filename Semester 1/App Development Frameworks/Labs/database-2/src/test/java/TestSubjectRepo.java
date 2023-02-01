import ie.szymon.entities.Result;
import ie.szymon.repo.SubjectRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/beans.xml"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSubjectRepo {
    @Autowired
    SubjectRepo subjectRepo;

    @Test
    @Order(6)
    public void delete() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(1, subjectRepo.delete(1)),
                ()-> Assertions.assertEquals(0, subjectRepo.delete(22))
        );
    }

    @Test
    @Order(1)
    public void findSubjectNameAndSchool() {
        Assertions.assertAll(
                ()-> Assertions.assertInstanceOf(Result.class, subjectRepo.findSubjectNameAndSchool(1)),
                ()-> Assertions.assertEquals("Math", subjectRepo.findSubjectNameAndSchool(1).subjectName()),
                ()-> Assertions.assertThrows(EmptyResultDataAccessException.class, ()-> subjectRepo.findSubjectNameAndSchool(55))
        );
    }

    @Test
    @Order(2)
    public void exists() {
        Assertions.assertAll(
                ()-> Assertions.assertTrue(subjectRepo.exists(1)),
                ()-> Assertions.assertFalse(subjectRepo.exists(22))
        );
    }

    @Test
    @Order(4)
    public void moveSchool() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(1, subjectRepo.moveSchool(1, 3)),
                ()-> Assertions.assertEquals(0, subjectRepo.moveSchool(22, 3)),
                ()-> Assertions.assertThrows(DataIntegrityViolationException.class, ()-> subjectRepo.moveSchool(1, 33))
        );
    }

    @Test
    @Order(3)
    public void findSubjectInSchool() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(0, subjectRepo.findSubjectsInSchool(33).size()),
                ()-> Assertions.assertEquals(2, subjectRepo.findSubjectsInSchool(3).size())
        );
    }

    @Test
    @Order(5)
    public void findAll() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(4, subjectRepo.findAll().size())
        );
    }
}
