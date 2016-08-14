package db.migration.v6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Expense {

    private Long id;
    private BigDecimal amount;
    private String debtors;
    private final String splitStrategy = "EVENLY";
    private List<Share> shares = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDebtors() {
        return debtors;
    }

    public void setDebtors(String debtors) {
        this.debtors = debtors;
    }

    public String getSplitStrategy() {
        return splitStrategy;
    }

    public String getSharesAsJSON(ObjectWriter objectWriter) {
        try {
            return objectWriter.writeValueAsString(shares);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculate() {

        if (debtors == null || debtors.isEmpty()) {
            return;
        }

        final String[] allDebtors = debtors.split(";");
        final int numberOfDebtors = allDebtors.length;

        final BigDecimal amountPerDebtor = amount.divide(new BigDecimal(numberOfDebtors));

        for (String debtor : allDebtors) {
            final Share share = new Share();

            share.setDebtor(debtor);
            share.setAmount(amountPerDebtor);

            shares.add(share);
        }
    }
}
