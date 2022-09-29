package ie.szymon.repo;

import ie.szymon.entities.Result;
import ie.szymon.entities.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public class SubjectRepoImpl implements SubjectRepo {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean exists(int subjectId) {
        String sql = "select count(*) from subject where subject_id = :subjectId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("subjectId", subjectId);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public int delete(int subjectId) {
        String sql = "delete subject where subject_id = :subjectId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("subjectId", subjectId);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int moveSchool(int subjectId, int newSchoolId) {
        return 0;
    }

    @Override
    public List<Subject> findSubjectsInSchool(int schoolId) {
        String sql = "select * from subject where school_id = :schoolId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("schoolId", schoolId);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource, new SubjectRowMapper());
    }

    @Override
    public Result findSubjectNameAndSchool(int subjectId) {
        return null;
    }

    @Override
    public List<Result> findAll() {
        return null;
    }
}
