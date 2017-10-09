package io.nobt.persistence.nobt;

import io.nobt.core.optimizer.Optimizer;
import io.nobt.persistence.cashflow.CashFlowEntity;
import io.nobt.persistence.cashflow.expense.ExpenseEntity;
import io.nobt.persistence.cashflow.payment.PaymentEntity;
import io.nobt.util.Sets;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nobts")
@Entity
public class NobtEntity {

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nobtName", nullable = false, length = 50)
    private String name;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "explicitParticipants")
    private String explicitParticipants;

    @Column(name = "createdOn", nullable = false)
    private ZonedDateTime createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = CashFlowEntity.class)
    private Set<ExpenseEntity> expenses = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = CashFlowEntity.class)
    private Set<PaymentEntity> payments = new HashSet<>();

    @Column(name = "optimizer")
    @Enumerated(EnumType.STRING)
    private Optimizer optimizer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public Set<PaymentEntity> getPayments() {
        return payments;
    }

    public void addPayment(PaymentEntity payment) {
        if (payments == null) {
            payments = new HashSet<>();
        }

        payments.add(payment);
        payment.setNobt(this);
    }

    public void addExpense(ExpenseEntity expense) {
        if (expenses == null) {
            expenses = new HashSet<>();
        }
        expenses.add(expense);
        expense.setNobt(this);
    }

    public void addExplicitParticipant(String participant) {
        if (explicitParticipants == null) {
            explicitParticipants = participant;
        } else {
            explicitParticipants = explicitParticipants + ";" + participant;
        }
    }

    public Set<String> getExplicitParticipants() {
        return explicitParticipants != null ? Sets.newHashSet(explicitParticipants.split(";")) : Collections.emptySet();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

	public Optimizer getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(Optimizer optimizer) {
		this.optimizer = optimizer;
	}
}
