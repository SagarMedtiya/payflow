package com.airtribe.payflow.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name = "upi_id", unique = true,nullable = false)
    private String upiId;

    @Column(name= "balance", nullable = false)
    private Double balance;

    @Column(name ="phone_number", length = 10)
    private String phoneNumber;

}
