package ie.szymon.repo;

import ie.szymon.entities.Movie;
import ie.szymon.entities.MovieAndDirector;
import ie.szymon.entities.Result;

import java.util.List;

public interface MovieRepo {
    List<Movie> getAll();
    boolean exists(int movieId);
    boolean existsByNameAndDirector(String title, int directorId);
    int addMovie(Movie newMovie);
    int changeTakings(int movieId, int takings);
    int deleteMovie(int movieId);
    MovieAndDirector findMovieAndDirector(int movieId);
    List<Movie> findMoviesByDirector(int directorId);
    float findAverageIncomeForDirector(int directorId);
    Result findHighestTakingsMovieAndDirector();
}
