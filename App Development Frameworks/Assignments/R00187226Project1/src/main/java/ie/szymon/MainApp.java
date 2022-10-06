package ie.szymon;

import ie.szymon.entities.Director;
import ie.szymon.service.DirectorService;
import ie.szymon.service.DirectorServiceImpl;
import ie.szymon.service.MovieService;
import ie.szymon.service.MovieServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        DirectorService directorService = context.getBean(DirectorServiceImpl.class);
        MovieService movieService = context.getBean(MovieServiceImpl.class);
        directorService.listAll().forEach(System.out::println);

        directorService.addDirector(new Director(5, "TestFirst", "TestLast", 0));
        directorService.listAll().forEach(System.out::println);

        directorService.deleteDirector(5);
        directorService.changeActive(2, 0);
        directorService.listAll().forEach(System.out::println);

        directorService.inactiveDirectors();

        movieService.listAll().forEach(System.out::println);
        System.out.printf("%.2f", movieService.averageIncomeForMovies(1));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(2));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(3));
        System.out.println();
        System.out.printf("%.2f", movieService.averageIncomeForMovies(4));
        System.out.println();

        System.out.println(movieService.highestTakingsMovie());
        System.out.println(movieService.findMovieInformation(1));
    }
}