package io.nobt.persistence.entity;

import io.nobt.util.Sets;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Table(name = "nobts")
@Entity
public class NobtEntity {

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nobtName", nullable = false, length = 50)
    private String name;

    @Column(name = "explicitParticipants")
    private String explicitParticipants;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExpenseEntity> expenses = new HashSet<>();

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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
