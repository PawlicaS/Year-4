package ie.szymon.repo;

import ie.szymon.entities.School;
import ie.szymon.rowmappers.SchoolRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SchoolRepoImpl implements SchoolRepo {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<School> getAll() {
        String sql = "select * from school";
        return namedParameterJdbcTemplate.query(sql, new SchoolRowMapper());
    }

    @Override
    public School findById(int id) {
        return null;
    }

    @Override
    public boolean exists(int id) {
        String sql = "select count(*) from school where school_id = :schoolId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("schoolId", id);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select count(*) from school where name = :name";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("name", name);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public int deleteSchool(int id) {
        String sql = "delete from school where school_id = :schoolId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("schoolId", id);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int createSchool(School newSchool) {
        String sql = "insert into school values (:id, :name, :number)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", newSchool.getSchoolId())
                .addValue("name", newSchool.getName())
                .addValue("number", newSchool.getNumber());
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int changeName(int id, String newName) {
        String sql = "update school set name = :newName where school_id = :schoolId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", newName);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
