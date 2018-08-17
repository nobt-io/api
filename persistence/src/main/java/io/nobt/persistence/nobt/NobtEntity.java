package io.nobt.persistence.nobt;

import io.nobt.core.domain.Person;
import io.nobt.core.optimizer.Optimizer;
import io.nobt.persistence.JacksonUtil;
import io.nobt.persistence.cashflow.expense.ExpenseEntity;
import io.nobt.persistence.cashflow.payment.PaymentEntity;
import io.nobt.persistence.json.NobtEntityModule;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nobts")
@Entity
@NamedQueries({
        @NamedQuery(name = "getByExternalId", query = "SELECT n FROM NobtEntity n WHERE n.externalId = :externalId")
})
public class NobtEntity {

    static {
        JacksonUtil.OBJECT_MAPPER.registerModule(new NobtEntityModule());
    }

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nobts_seq")
    private Long id;

    @Column(name = "nobtName", nullable = false, length = 50)
    private String name;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "explicitParticipants")
    private Set<Person> explicitParticipants = new HashSet<>();

    @Column(name = "createdOn", nullable = false, updatable = false)
    private Instant createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL)
    private Set<ExpenseEntity> expenses = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentEntity> payments = new HashSet<>();

    @Column(name = "optimizer")
    @Enumerated(EnumType.STRING)
    private Optimizer optimizer;

    @Column(name = "externalId", length = 20, nullable = false, unique = true, updatable = false)
    private String externalId;

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

    public void setExplicitParticipant(Set<Person> participants) {
        explicitParticipants = participants;
    }

    public Set<Person> getExplicitParticipants() {
        return explicitParticipants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Optimizer getOptimizer() {
        return optimizer;
    }

    public void setOptimizer(Optimizer optimizer) {
        this.optimizer = optimizer;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
