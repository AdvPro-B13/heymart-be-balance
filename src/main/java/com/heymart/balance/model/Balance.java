package com.heymart.balance.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@NoArgsConstructor
@Entity
@Table(name = "Balance")
@Getter
@Setter
public class Balance {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToMany(mappedBy = "balance", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Transaction> transactions;

    public enum OwnerType {
        USER, SUPERMARKET
    }

    public Balance(UUID ownerId, OwnerType ownerType) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.balance = 0.0;
    }
}

