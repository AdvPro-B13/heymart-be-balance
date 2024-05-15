package com.heymart.balance.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Supermarket")
@NoArgsConstructor
public class Supermarket {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public Supermarket(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
