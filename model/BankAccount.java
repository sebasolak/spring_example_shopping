package com.example.shopping.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal founds;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public BankAccount(BigDecimal founds,
                       AppUser appUser) {
        this.founds = founds;
        this.appUser = appUser;
    }

    public BankAccount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getFounds() {
        return founds;
    }

    public void setFounds(BigDecimal founds) {
        this.founds = founds;
    }
}
