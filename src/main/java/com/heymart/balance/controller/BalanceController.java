package com.heymart.balance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
    @GetMapping("/")
    public String home() {
        return "hello from Heymart Balance";
    }
}