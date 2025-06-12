package model;

import java.util.*;

public class Client {
    private String name;
    private String cpf;
    private String password;
    private ArrayList<Account> accounts;

    public Client(String name, String cpf, String password) {
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }
}