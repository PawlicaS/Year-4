package ie.szymon.rowmappers;

import ie.szymon.entities.Result;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultRowMapper implements RowMapper<Result> {
    @Override
    public Result mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Result(rs.getString("director.first_name"), rs.getString("director.last_name"), rs.getInt("movie.takings"), rs.getString("movie.title"));
    }
}
