package model;

public class CurrentAccount extends Account {
    private double creditLimit;

    public CurrentAccount(Client client) {
        super("Corrente", client);
        this.creditLimit = 100.0; // Exemplo de limite de crédito
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public double calculateWithdrawalLimit() {
        return getBalance() + creditLimit;
    }

    @Override
    public double getTaxableInterest() {
        return 0.0;
    }

    @Override
    public void deductTaxableInterest(double amount) {
        // Não aplicável para conta corrente
    }
}


