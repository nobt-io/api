package db.migration.v6;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.List;

public class V6__Migrate_expenses_to_jsonb_shares implements SpringJdbcMigration, MigrationChecksumProvider {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        final ObjectWriter objectWriter = new ObjectMapper().writer();

        final List<Expense> expenses = jdbcTemplate.query("SELECT * FROM expenses", new ExpenseRowMapper());

        for (Expense expense : expenses) {

            expense.recalculate();

            jdbcTemplate.update("UPDATE expenses SET shares = ?, splitStrategy = ? WHERE id = ?", ps -> {
                ps.setObject(1, expense.getSharesAsJSON(objectWriter), Types.OTHER);
                ps.setString(2, expense.getSplitStrategy());
                ps.setLong(3, expense.getId());
            });
        }
    }

    @Override
    public Integer getChecksum() {
        return V6__Migrate_expenses_to_jsonb_shares.class.getName().hashCode();
    }
}
