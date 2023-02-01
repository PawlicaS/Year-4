package ie.szymon.rowmappers;

import ie.szymon.entities.School;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchoolRowMapper implements RowMapper<School> {

    @Override
    public School mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new School(rs.getInt(1), rs.getString(2), rs.getInt(3));
    }
}
