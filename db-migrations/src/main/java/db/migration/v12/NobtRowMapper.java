package db.migration.v12;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NobtRowMapper implements RowMapper<Nobt> {

    @Override
    public Nobt mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Nobt(
                rs.getLong("id"),
                rs.getString("explicitParticipants_legacy")
        );
    }
}
