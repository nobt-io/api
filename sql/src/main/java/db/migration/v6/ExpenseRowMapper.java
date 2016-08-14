package db.migration.v6;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Thomas Eizinger, Senacor Technologies AG.
 */
public class ExpenseRowMapper implements RowMapper<Expense> {
    @Override
    public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {

        final Expense expense = new Expense();

        expense.setId(rs.getLong("id"));
        expense.setAmount(rs.getBigDecimal("amount"));
        expense.setDebtors(rs.getString("debtors"));

        return expense;
    }
}
