package model;

import java.util.*;

public class Bank {
    private String name;
    private ArrayList<Client> clients;

    public Bank(String name) {
        this.name = name;
        this.clients = new ArrayList<Client>();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }

    public void applyMonthlyInterestsToAllAccounts() {
        for (Client client : clients) {
            for (Account account : client.getAccounts()) {
                account.applyMonthlyInterest();
            }
        }
    }
}


