package ie.szymon.repo;

import ie.szymon.entities.Director;
import ie.szymon.rowmappers.DirectorRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DirectorRepoImpl implements DirectorRepo {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Director> getAll() {
        String sql = "select * from director";
        return namedParameterJdbcTemplate.query(sql, new DirectorRowMapper());
    }

    @Override
    public boolean exists(int directorId) {
        String sql = "select count(*) from director where director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("directorId", directorId);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public boolean existsByName(String firstName, String lastName) {
        String sql = "select count(*) from director where first_name+last_name = :fullName+:lastName";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("first_name", firstName).addValue("last_name", lastName);
        Integer number = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return number != null && number == 1;
    }

    @Override
    public int createDirector(Director newDirector) {
        String sql = "insert into director values (:directorId, :firstName, :lastName, :active)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("directorId", newDirector.getDirectorId())
                .addValue("firstName", newDirector.getFirstName())
                .addValue("lastName", newDirector.getLastName())
                .addValue("active", newDirector.isActive());
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int deleteDirector(int directorId) {
        String sql = "delete from director where director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("directorId", directorId);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int changeActive(int directorId, boolean newActive) {
        String sql = "update director set active = :newActive where director_id = :directorId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("director_id", directorId)
                .addValue("active", newActive);
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public int findInactiveDirectors() {
        String sql = "select count(*) from director where active = :active";
        return namedParameterJdbcTemplate.query(sql, new DirectorRowMapper()).size();
    }
}
