package com.example.bank.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2019-08-13 17:42
 **/
@Data
@Entity
@Table(name = "bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Integer amount;
}
