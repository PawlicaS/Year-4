package ie.szymon.rowmappers;

import ie.szymon.entities.Subject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectRowMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setName(rs.getString("name"));
        subject.setEmail(rs.getString("email"));
        subject.setSchoolId(rs.getInt("school_id"));
        return subject;
    }
}
