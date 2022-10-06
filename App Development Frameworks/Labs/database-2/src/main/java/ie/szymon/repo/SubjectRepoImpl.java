package ie.szymon.repo;

import ie.szymon.entities.Result;
import ie.szymon.entities.Subject;
import ie.szymon.rowmappers.ResultRowMapper;
import ie.szymon.rowmappers.SubjectRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
        String sql = "update subject set school_id = :newSchoolId where subject_id = :subjectId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("newSchoolId", newSchoolId)
                .addValue("subjectId", subjectId);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public List<Subject> findSubjectsInSchool(int schoolId) {
        String sql = "select * from subject where school_id = :schoolId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("schoolId", schoolId);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new SubjectRowMapper());
    }

    @Override
    public Result findSubjectNameAndSchool(int subjectId) {
        String sql = "select s.name, c.name from subject c inner join school s on s.school_id = c.school_id where c.subject_id = :subjectId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("subjectId", subjectId);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new ResultRowMapper());
    }

    @Override
    public List<Result> findAll() {
        String sql = "select s.name, c.name from subject c inner join school s on s.school_id = c.school_id";
        return namedParameterJdbcTemplate.getJdbcTemplate().query(sql, new ResultRowMapper());
    }
}
