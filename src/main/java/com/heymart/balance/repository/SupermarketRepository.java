package com.heymart.balance.repository;

import com.heymart.balance.model.Supermarket;
import com.heymart.balance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SupermarketRepository extends JpaRepository<Supermarket, UUID> {
}
