package db.migration.v6;

import java.math.BigDecimal;

public class Share {

    private String debtor;
    private BigDecimal amount;

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
