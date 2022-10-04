package ie.szymon;

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
    }
}