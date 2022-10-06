package ie.szymon.repo;

import ie.szymon.entities.Movie;
import ie.szymon.entities.MovieAndDirector;
import ie.szymon.entities.Result;
import ie.szymon.rowmappers.DirectorRowMapper;
import ie.szymon.rowmappers.MovieAndDirectorRowMapper;
import ie.szymon.rowmappers.MovieRowMapper;
import ie.szymon.rowmappers.ResultRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepoImpl implements MovieRepo {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Movie> getAll() {
        String sql = "select * from movie";
        return namedParameterJdbcTemplate.query(sql, new MovieRowMapper());
    }

    @Override
    public boolean exists(int movieId) {
        String sql = "select count(*) from movie where movie_id = :movieId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("movieId", movieId);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }
    @Override
    public boolean existsByNameAndDirector(String title, int directorId) {
        String sql = "select count(*) from movie where title = :title and director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("title", title).addValue("director_id", directorId);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public int addMovie(Movie newMovie) {
        String sql = "insert into movie values (:movieId, :title, :release_year, :takings, :director_id)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("movieId", newMovie.getMovieId())
                .addValue("title", newMovie.getTitle())
                .addValue("release_year", newMovie.getReleaseYear())
                .addValue("takings", newMovie.getTakings())
                .addValue("director_id", newMovie.getDirectorId());
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int changeTakings(int movieId, int takings) {
        String sql = "update movie set active = :takings where movie_id = :movieId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("director_id", movieId)
                .addValue("takings", takings);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int deleteMovie(int movieId) {
        String sql = "delete movie where movie_id = :movieId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("movieId", movieId);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public MovieAndDirector findMovieAndDirector(int movieId) {
        String sql = "select d.first_name, d.last_name, m.title, m.release_year, m.takings from movie m inner join director d on d.director_id = m.director_id where m.movie_id = :movieId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("movieId", movieId);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new MovieAndDirectorRowMapper());
    }

    @Override
    public List<Movie> findMoviesByDirector(int directorId) {
        String sql = "select * from movie where director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("directorId", directorId);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new MovieRowMapper());
    }

    @Override
    public float findAverageIncomeForDirector(int directorId) {
        String sql = "select avg(takings) from movie where director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("directorId", directorId);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Float.class);
    }

    @Override
    public Result findHighestTakingsMovieAndDirector() {
        String sql = "select d.first_name, d.last_name, m.takings, m.title from movie m inner join director d on d.director_id = m.director_id where m.takings = select max(takings) from movie";
        SqlParameterSource sqlParameterSource = new EmptySqlParameterSource();
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new ResultRowMapper());
    }
}
