package com.heymart.balance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@Entity
@Table(name = "Transaction")
@Setter
@Getter
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "balance_id", nullable = false)
    @JsonIgnore
    private Balance balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date", nullable = false, updatable = false)
    private Date transactionDate;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    public enum OwnerType {
        USER, SUPERMARKET
    }

    public enum TransactionType {
        TOPUP, WITHDRAWAL
    }

    public Transaction(UUID ownerId, OwnerType ownerType, Date transactionDate, double amount, TransactionType transactionType) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    @PrePersist
    protected void onCreate() {
        transactionDate = new Date();
    }
}
