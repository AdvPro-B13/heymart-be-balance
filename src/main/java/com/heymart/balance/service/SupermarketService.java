package com.heymart.balance.service;

import com.heymart.balance.model.Supermarket;
import com.heymart.balance.repository.SupermarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupermarketService {
    @Autowired
    private SupermarketRepository supermarketRepository;

    public Supermarket saveSupermarket(Supermarket Supermarket) {
        return supermarketRepository.save(Supermarket);
    }
    public Optional<Supermarket> getSupermarketById(UUID id) {
        return supermarketRepository.findById(id);
    }

    public List<Supermarket> getAllSupermarkets() {
        return supermarketRepository.findAll();
    }

    public void deleteSupermarket(UUID id) {
        supermarketRepository.deleteById(id);
    }

}
