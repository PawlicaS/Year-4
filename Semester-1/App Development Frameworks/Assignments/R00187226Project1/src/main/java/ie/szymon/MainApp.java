package ie.szymon;

import ie.szymon.entities.Director;
import ie.szymon.entities.Movie;
import ie.szymon.service.DirectorService;
import ie.szymon.service.DirectorServiceImpl;
import ie.szymon.service.MovieService;
import ie.szymon.service.MovieServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
@Slf4j
public class MainApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        DirectorService directorService = context.getBean(DirectorServiceImpl.class);
        MovieService movieService = context.getBean(MovieServiceImpl.class);

        //List all directors
        System.out.println("--List all directors--");
        directorService.listAll().forEach(System.out::println);
        System.out.println();

        //Add a director
        System.out.println("--Add a director--");
        directorService.addDirector(new Director(5, "TestFirst", "TestLast", 0));
        directorService.listAll().forEach(System.out::println);
        System.out.println();

        //Delete director given their ID
        System.out.println("--Delete director given their ID--");
        directorService.deleteDirector(5);
        directorService.listAll().forEach(System.out::println);
        System.out.println();

        //Change a director’s active status given their ID
        System.out.println("--Change a director’s active status given their ID--");
        directorService.changeActive(2, 0);
        directorService.listAll().forEach(System.out::println);
        System.out.println();

        //Determine the number of inactive directors
        System.out.println("--Determine the number of inactive directors--");
        System.out.println(directorService.inactiveDirectors());
        System.out.println();

        //List all movies
        System.out.println("--List all movies--");
        movieService.listAll().forEach(System.out::println);
        System.out.println();

        //Add a movie assigning it to a specific director
        System.out.println("--Add a movie assigning it to a specific director--");
        movieService.addMovie(new Movie(7, "Test", 100, 1000, 2));
        movieService.listAll().forEach(System.out::println);
        System.out.println();

        //Delete a movie given its ID
        System.out.println("--Delete a movie given its ID--");
        movieService.deleteMovie(7);
        movieService.listAll().forEach(System.out::println);
        System.out.println();

        //Find a movie by its ID showing all information and its director
        System.out.println("--Find a movie by its ID showing all information and its director--");
        System.out.println(movieService.findMovieInformation(1));
        System.out.println();

        //Find all movies by a director given the director's ID
        System.out.println("--Find all movies by a director given the director's ID--");
        System.out.println(movieService.findMoviesByDirector(2));
        System.out.println();

        //Modify a movie's takings given its ID
        System.out.println("--Modify a movie's takings given its ID--");
        movieService.modifyTakings(2, 12341234);
        movieService.listAll().forEach(System.out::println);
        System.out.println();

        //Determine the average income for all movies by a particular director
        System.out.println("--Determine the average income for all movies by a particular director--");
        System.out.printf("%.2f", movieService.averageIncomeForMovies(1));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(2));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(3));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(4));
        System.out.println("\n");

        //Determine the name of the movie with the highest takings along with the name of its director
        System.out.println("--Determine the name of the movie with the highest takings along with the name of its director--");
        System.out.println(movieService.highestTakingsMovie());
    }
}