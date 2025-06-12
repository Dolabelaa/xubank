package model;

import java.util.*;

public class Transaction {
    private Date date;
    private String type;
    private double amount;
    private Account account;

    public Transaction(String type, double amount, Account account) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.account = account;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }
}