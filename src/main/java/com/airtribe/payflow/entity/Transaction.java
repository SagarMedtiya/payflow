package com.airtribe.payflow.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "sender_upi_id", nullable = false)
    private String senderUpiId;

    @Column(name="receiver_upi_id", nullable = false)
    private String receiverUpiId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name="note")
    private String note;
}
