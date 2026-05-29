package com.airtribe.payflow.controller;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<Transaction> sendMoney(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.sendMoney(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }
}


























