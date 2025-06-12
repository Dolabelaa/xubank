package model;

import java.util.*;

public abstract class Account {
    protected Double balance;
    protected String category; 
    protected Client client;
    protected ArrayList<Transaction> transactions;

    public Account(String category, Client client) {
        this.balance = 0.0;
        this.category = category;
        this.client = client;
        this.transactions = new ArrayList<Transaction>();
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCategory() {
        return category;
    }

    public Client getClient() {
        return client;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void applyMonthlyInterest() {
        // Default implementation, overridden by subclasses
    }

    public abstract double calculateWithdrawalLimit();

    public abstract double getTaxableInterest();

    public abstract void deductTaxableInterest(double amount);

    public void deposit(double amount) {
        this.balance += amount;
        addTransaction(new Transaction("Depósito", amount, this));
    }

    public boolean withdraw(double amount) {
        if (calculateWithdrawalLimit() >= amount) {
            this.balance -= amount;
            addTransaction(new Transaction("Saque", amount, this));
            return true;
        }
        return false;
    }
}


