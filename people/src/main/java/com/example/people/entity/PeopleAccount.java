package com.example.people.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2019-08-14 10:27
 **/
@Data
@Entity
@Table(name = "people_account")
public class PeopleAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int amount;
}
