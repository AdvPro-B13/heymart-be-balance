package com.heymart.balance.controller;

import com.heymart.balance.model.Supermarket;
import com.heymart.balance.service.SupermarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/supermarket")
public class SupermarketController {
    @Autowired
    SupermarketService supermarketService;

    @PostMapping
    public ResponseEntity<Supermarket> createUser(@RequestBody Supermarket supermarket) {
        Supermarket savedSupermarket = supermarketService.saveSupermarket(supermarket);
        return ResponseEntity.ok(savedSupermarket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supermarket> getUserById(@PathVariable UUID id) {
        return supermarketService.getSupermarketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
