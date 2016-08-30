package io.nobt.persistence.entity;

import io.nobt.util.Sets;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nobts")
@Entity
public class NobtEntity {

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "nobtName", nullable = false, length = 50)
    private String name;

    @Column(name = "explicitParticipants")
    private String explicitParticipants;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL)
    private Set<ExpenseEntity> expenses = new HashSet<>();

    public NobtEntity(String name, Set<String> explicitParticipants) {
        this.name = name;
        this.explicitParticipants = String.join(";", explicitParticipants);
    }

    public NobtEntity() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void addExpense(ExpenseEntity expense) {
        if (expenses == null) {
            expenses = new HashSet<>();
        }
        expenses.add(expense);
        expense.setNobt(this);
    }

    public Set<String> getExplicitParticipants() {
        return explicitParticipants != null ? Sets.newHashSet(explicitParticipants.split(";")) : Collections.emptySet();
    }

    public long getId() {
        return id;
    }
}
