import ie.szymon.entities.Director;
import ie.szymon.entities.Movie;
import ie.szymon.entities.Result;
import ie.szymon.service.DirectorService;
import ie.szymon.service.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/beans.xml"})
public class ServiceTest {
    @Autowired
    DirectorService directorService;
    @Autowired
    MovieService movieService;

    @Test
    public void directorTest() {
        Assertions.assertAll(
                ()-> Assertions.assertTrue(directorService.deleteDirector(4)),
                ()-> Assertions.assertFalse(directorService.deleteDirector(5)),
                ()-> Assertions.assertFalse(directorService.addDirector(new Director(1, "Test", "Director", 1))),
                ()-> Assertions.assertTrue(directorService.addDirector(new Director(5, "Test", "Director", 1))),
                ()-> Assertions.assertFalse(directorService.addDirector(new Director(6, "Test", "Director", 0))),
                ()-> Assertions.assertTrue(directorService.changeActive(1, 0)),
                ()-> Assertions.assertFalse(directorService.changeActive(1, 99)),
                ()-> Assertions.assertFalse(directorService.changeActive(99, 1))
        );
    }

    @Test
    public void averageIncomeTest() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(3.73766048E8, movieService.averageIncomeForMovies(1)),
                ()-> Assertions.assertEquals(0, movieService.averageIncomeForMovies(99))
        );
    }

    @Test
    public void inactiveDirectorTest() {
        Assertions.assertAll(
                ()-> Assertions.assertEquals(1, directorService.inactiveDirectors()),
                ()-> Assertions.assertTrue(directorService.changeActive(2, 0)),
                ()-> Assertions.assertEquals(2, directorService.inactiveDirectors())
        );
    }

    @Test
    public void highestTakingsTest() {
        Assertions.assertAll(
                ()-> Assertions.assertInstanceOf(Result.class, movieService.highestTakingsMovie()),
                ()-> Assertions.assertEquals("Steven", movieService.highestTakingsMovie().directorFirstName())
        );
    }

    @Test
    public void movieTest() {
        Assertions.assertAll(
                ()-> Assertions.assertTrue(movieService.addMovie(new Movie(7, "TestMovie", 2000, 12341234, 1))),
                ()-> Assertions.assertTrue(movieService.addMovie(new Movie(8, "TestMovie", 2000, 12341234, 2))),
                ()-> Assertions.assertFalse(movieService.addMovie(new Movie(9, "TestMovie", 2000, 12341234, 2))),
                ()-> Assertions.assertFalse(movieService.addMovie(new Movie(1, "TestMovie2", 2000, 12341234, 2))),
                ()-> Assertions.assertInstanceOf(Optional.class, movieService.findMoviesByDirector(1)),
                ()-> Assertions.assertTrue(movieService.findMoviesByDirector(1).isPresent()),
                ()-> Assertions.assertFalse(movieService.findMoviesByDirector(99).isPresent()),
                ()-> Assertions.assertEquals(3, movieService.findMoviesByDirector(1).get().size()),
                ()-> Assertions.assertThrows(NoSuchElementException.class, ()-> movieService.findMoviesByDirector(99).get().size()),
                ()-> Assertions.assertTrue(movieService.modifyTakings(1, 12341234)),
                ()-> Assertions.assertFalse(movieService.modifyTakings(99, 12341234))
        );
    }
}
