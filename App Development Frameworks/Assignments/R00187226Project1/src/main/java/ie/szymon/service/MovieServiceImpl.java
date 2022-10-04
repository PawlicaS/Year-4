package ie.szymon.service;

import ie.szymon.entities.Movie;
import ie.szymon.entities.MovieAndDirector;
import ie.szymon.entities.Result;
import ie.szymon.repo.DirectorRepo;
import ie.szymon.repo.MovieRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    @Autowired
    MovieRepo movieRepo;

    @Autowired
    DirectorRepo directorRepo;

    @Override
    public List<Movie> listAll() {
        return movieRepo.getAll();
    }

    @Override
    public boolean addMovie(Movie newMovie) {
        if (movieRepo.existsByNameAndDirector(newMovie.getTitle(), newMovie.getDirectorId())) {
            log.error("Could not add movie because a movie called " + newMovie.getTitle() + " already exists by that director");
            return false;
        }
        if (! directorRepo.exists(newMovie.getDirectorId())) {
            log.error("Could not add movie because a director with id " + newMovie.getDirectorId() + " does not exist");
            return false;
        }
        if (movieRepo.exists(newMovie.getMovieId())) {
            log.error("Could not add movie because a movie with id " + newMovie.getMovieId() + " already exists");
            return false;
        }
        return movieRepo.addMovie(newMovie) == 1;
    }

    @Override
    public boolean deleteMovie(int movieId) {
        if (movieRepo.exists(movieId)) {
            return movieRepo.deleteMovie(movieId) == 1;
        }
        log.error("Could not delete movie with id " + movieId + " because it does not exist");
        return false;
    }

    @Override
    public Optional<MovieAndDirector> findMovieInformation(int movieId) {
        return movieRepo.exists(movieId)?
                Optional.of(movieRepo.findMovieAndDirector(movieId))
                : Optional.empty();
    }

    @Override
    public Optional<List<Movie>> findMoviesByDirector(int directorId) {
        if (movieRepo.exists(directorId)) {
            List<Movie> movies = movieRepo.findMoviesByDirector(directorId);
            return movies.isEmpty()? Optional.empty(): Optional.of(movies);
        }
        return Optional.empty();
    }

    @Override
    public boolean modifyTakings(int movieId, int takings) {
        if (! movieRepo.exists(movieId)) {
            log.error("Could not change movie takings because a movie with id " + movieId + " does not exist");
            return false;
        }
        return movieRepo.changeTakings(movieId, takings) == 1;
    }

    @Override
    public float averageIncomeForMovies(int directorId) {
        if (! directorRepo.exists(directorId)) {
            log.error("Could not get average from director because a director with id " + directorId + " does not exist");
            return 0;
        }
        return movieRepo.findAverageIncomeForDirector(directorId);
    }

    @Override
    public Result highestTakingsMovie() {
        return movieRepo.findHighestTakingsMovieAndDirector();
    }
}
