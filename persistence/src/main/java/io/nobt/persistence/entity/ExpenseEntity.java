package io.nobt.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Matthias
 */
@Table(name = "expenses")
@Entity
public class ExpenseEntity {

    @Id
    @SequenceGenerator(name = "expense_seq", sequenceName = "expense_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "expenseName", nullable = false, length = 50)
    private String name;

    @Column(name = "debtee", nullable = false)
    private String debtee;

    @Column(name = "splitStrategy", nullable = false)
    private String splitStrategy;

    @ManyToOne
    @JoinColumn(name = "NOBT_ID", nullable = false)
    private NobtEntity nobt;

    @Column(name = "shares")
    private byte[] shares;

    public long getId() {
        return id;
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

    public NobtEntity getNobt() {
        return nobt;
    }

    public void setNobt(NobtEntity nobt) {
        this.nobt = nobt;
    }

    public byte[] getShares() {
        return shares;
    }

    public void setShares(byte[] shares) {
        this.shares = shares;
    }
}