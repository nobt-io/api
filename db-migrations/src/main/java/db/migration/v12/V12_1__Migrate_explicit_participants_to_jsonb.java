package db.migration.v12;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class V12_1__Migrate_explicit_participants_to_jsonb implements SpringJdbcMigration, MigrationChecksumProvider {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        final ObjectWriter objectWriter = new ObjectMapper().writer();

        final List<Nobt> nobts = jdbcTemplate.query("SELECT id,explicitParticipants_legacy FROM nobts", new NobtRowMapper());

        nobts.forEach(Nobt::convert);

        jdbcTemplate.batchUpdate("UPDATE nobts SET explicitParticipants = ? WHERE id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                final Nobt currentNobt = nobts.get(i);

                ps.setObject(1, currentNobt.getExplicitParticipantsAsJSON(objectWriter), Types.OTHER);
                ps.setLong(2, currentNobt.getId());

            }

            @Override
            public int getBatchSize() {
                return nobts.size();
            }
        });

    }

    @Override
    public Integer getChecksum() {
        return V12_1__Migrate_explicit_participants_to_jsonb.class.getName().hashCode();
    }
}
