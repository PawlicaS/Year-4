package ie.szymon.rowmappers;

import ie.szymon.entities.MovieAndDirector;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieAndDirectorRowMapper implements RowMapper<MovieAndDirector> {
    @Override
    public MovieAndDirector mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MovieAndDirector(rs.getString("director.first_name"), rs.getString("director.last_name"),
                rs.getString("movie.title"), rs.getInt("movie.release_year"), rs.getInt("movie.takings"));
    }
}
