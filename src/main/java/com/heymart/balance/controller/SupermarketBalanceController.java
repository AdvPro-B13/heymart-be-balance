package com.heymart.balance.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.heymart.balance.dto.AmountDTO;
import com.heymart.balance.model.Supermarket;
import com.heymart.balance.model.SupermarketBalance;
import com.heymart.balance.service.SupermarketBalanceService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/supermarket-balance")
public class SupermarketBalanceController {

    @Autowired
    private SupermarketBalanceService service;

    @GetMapping("/")
    public String home() {
        return "you are accessing supermarket balance api";
    }

    @PostMapping("/{marketname}/")
    public ResponseEntity<SupermarketBalance> postSupermarketBalance(@PathVariable String marketname) {
        SupermarketBalance newBalance = new SupermarketBalance(new Supermarket(marketname));
        newBalance.setId(UUID.fromString("d4fff456-2665-4191-b5f5-bcfbffec4eb6"));
        SupermarketBalance created = service.createBalance(newBalance);

        if (created == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}/")
    public CompletableFuture<ResponseEntity<?>> getBalanceById(@PathVariable String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }

        return service.findById(uuid)
                .thenApply(balance -> {
                    if (balance == null) {
                        return ResponseEntity.<SupermarketBalance>notFound().build();
                    }
                    return ResponseEntity.ok(balance);
                })
                .exceptionally(ex -> ResponseEntity.<SupermarketBalance>status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

//    @PutMapping("/{id}/topup/")
//    public ResponseEntity<SupermarketBalance> putTopupBalance(@PathVariable String id, @Valid @RequestBody AmountDTO amountDTO) {
//        UUID uuid;
//        try {
//            uuid = UUID.fromString(id);
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        SupermarketBalance balance = service.findById(uuid);
//
//        if (balance != null) {
//            service.topup(balance, amountDTO.getAmount());
//            return ResponseEntity.ok(balance);
//        }
//
//        return ResponseEntity.notFound().build();
//    }

//    @PutMapping("/{id}/withdraw/")
//    public ResponseEntity<SupermarketBalance> putWithdrawBalance(@PathVariable String id, @Valid @RequestBody AmountDTO amountDTO) {
//        UUID uuid;
//        try {
//            uuid = UUID.fromString(id);
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        SupermarketBalance balance = service.findById(uuid);
//
//        if (balance != null) {
//            SupermarketBalance result = service.withdraw(balance, amountDTO.getAmount());
//
//            if (result == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            return ResponseEntity.ok(result);
//        }
//        return ResponseEntity.notFound().build();
//    }
}