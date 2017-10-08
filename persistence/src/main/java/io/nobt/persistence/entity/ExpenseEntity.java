package io.nobt.persistence.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Matthias
 */
@Table(name = "expenses")
@Entity
public class ExpenseEntity {

    @Embeddable
    public static class Key implements Serializable {

        @Column(name = "id")
        private long id;

        @ManyToOne
        @JoinColumn(name = "nobtId", updatable = false, insertable = false, nullable = false)
        private NobtEntity nobt;

        public Key() {
        }

        public Key(long id, NobtEntity nobt) {
            this.id = id;
            this.nobt = nobt;
        }

        public long getId() {
            return id;
        }

        public NobtEntity getNobt() {
            return nobt;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setNobt(NobtEntity nobtId) {
            this.nobt = nobtId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(id, key.id) &&
                    Objects.equals(nobt, key.nobt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, nobt);
        }
    }

    @EmbeddedId
    private Key id;

    @Column(name = "legacyId", nullable = false)
    private long legacyId;

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

    @ManyToOne
    @JoinColumn(name = "nobtId", nullable = false, insertable = false, updatable = false)
    private NobtEntity nobt;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "shares")
    private List<ShareEntity> shares;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "createdOn", nullable = false)
    private ZonedDateTime createdOn;

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

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

    public NobtEntity getNobt() {
        return nobt;
    }

    public void setNobt(NobtEntity nobt) {
        this.nobt = nobt;
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

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }
}