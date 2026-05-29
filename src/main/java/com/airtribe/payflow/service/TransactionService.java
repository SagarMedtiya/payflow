package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction sendMoney(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
