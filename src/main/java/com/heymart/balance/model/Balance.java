package com.heymart.balance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @JsonIgnore
    @OneToMany(mappedBy = "balance", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Transaction> transactions;

    public void setAmount(double v) {
    }

    public enum OwnerType {
        USER, SUPERMARKET
    }

    public Balance(String ownerId, OwnerType ownerType) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.balance = 0.0;
    }
}

