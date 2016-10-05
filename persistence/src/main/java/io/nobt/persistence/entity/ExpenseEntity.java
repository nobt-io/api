package io.nobt.persistence.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "shares")
    private List<ShareEntity> shares;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}