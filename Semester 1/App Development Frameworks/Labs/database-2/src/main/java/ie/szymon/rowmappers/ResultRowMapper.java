package ie.szymon.rowmappers;

import ie.szymon.entities.Result;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultRowMapper implements RowMapper<Result> {
    @Override
    public Result mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Result(rs.getString("school.name"), rs.getString("subject.name"));
    }
}
