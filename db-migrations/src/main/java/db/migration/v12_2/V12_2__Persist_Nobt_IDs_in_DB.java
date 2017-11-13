package db.migration.v12_2;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class V12_2__Persist_Nobt_IDs_in_DB implements SpringJdbcMigration, MigrationChecksumProvider {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        final List<Long> ids = jdbcTemplate.query("SELECT id from nobts", (rs, rowNum) -> rs.getLong("id"));

        jdbcTemplate.batchUpdate("UPDATE nobts SET externalId = ? WHERE id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                final Long currentId = ids.get(i);

                final String externalId = ShortURL.encode(PseudoCrypter.pseudoCryptLong(currentId));

                ps.setString(1, externalId);
                ps.setLong(2, currentId);
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    @Override
    public Integer getChecksum() {
        return V12_2__Persist_Nobt_IDs_in_DB.class.getName().hashCode();
    }
}
