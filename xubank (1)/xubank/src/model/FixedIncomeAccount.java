package model;

import java.util.Random;

public class FixedIncomeAccount extends Account {
    private double accumulatedInterest; // Para acumular o rendimento tributável

    public FixedIncomeAccount(Client client) {
        super("Renda Fixa", client);
        this.accumulatedInterest = 0.0;
    }

    @Override
    public void applyMonthlyInterest() {
        Random rand = new Random();
        double interestRate = 0.0050 + (0.0085 - 0.0050) * rand.nextDouble(); // 0.50% a 0.85% mensal
        double interest = getBalance() * interestRate;
        setBalance(getBalance() + interest);
        addTransaction(new Transaction("Rendimento", interest, this));
        this.accumulatedInterest += interest; // Acumula o rendimento

        // Cobra R$20 do cliente, mensalmente.
        setBalance(getBalance() - 20.0);
        addTransaction(new Transaction("Tarifa Mensal", -20.0, this));
    }

    @Override
    public double calculateWithdrawalLimit() {
        return getBalance();
    }

    @Override
    public double getTaxableInterest() {
        return this.accumulatedInterest;
    }

    @Override
    public void deductTaxableInterest(double amount) {
        this.accumulatedInterest -= amount;
        if (this.accumulatedInterest < 0) {
            this.accumulatedInterest = 0;
        }
    }
}


