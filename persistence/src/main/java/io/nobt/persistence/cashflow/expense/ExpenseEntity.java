package io.nobt.persistence.cashflow.expense;

import io.nobt.persistence.cashflow.CashFlowEntity;
import io.nobt.persistence.share.ShareEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Table(name = "expenses")
@Entity
public class ExpenseEntity extends CashFlowEntity {

    @Column(name = "expenseName", nullable = false, length = 50)
    private String name;

    @Column(name = "debtee", nullable = false)
    private String debtee;

    @Column(name = "splitStrategy", nullable = false)
    private String splitStrategy;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "conversionRate", nullable = false)
    private BigDecimal conversionRate;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "shares")
    private List<ShareEntity> shares;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDebtee() {
        return debtee;
    }

    public void setDebtee(String debtee) {
        this.debtee = debtee;
    }

    public String getSplitStrategy() {
        return splitStrategy;
    }

    public void setSplitStrategy(String splitStrategy) {
        this.splitStrategy = splitStrategy;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    public List<ShareEntity> getShares() {
        return shares;
    }

    public void setShares(List<ShareEntity> shares) {
        this.shares = shares;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isNotDeleted() {
        return !isDeleted();
    }
}