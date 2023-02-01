package ie.szymon.service;

import ie.szymon.entities.Movie;
import ie.szymon.entities.MovieAndDirector;
import ie.szymon.entities.Result;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> listAll();
    boolean addMovie(Movie newMovie);
    boolean deleteMovie(int movieId);
    Optional<MovieAndDirector> findMovieInformation(int movieId);
    Optional<List<Movie>> findMoviesByDirector(int directorId);
    boolean modifyTakings(int movieId, int takings);
    float averageIncomeForMovies(int directorId);
    Result highestTakingsMovie();
}
