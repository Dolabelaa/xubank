package model;

import java.util.Random;

public class InvestmentAccount extends Account {
    private double accumulatedInterest; // Para acumular o rendimento tributável

    public InvestmentAccount(Client client) {
        super("Investimento", client);
        this.accumulatedInterest = 0.0;
    }

    @Override
    public void applyMonthlyInterest() {
        Random rand = new Random();
        double interestRate = -0.0060 + (0.0150 - (-0.0060)) * rand.nextDouble(); // -0.60% a 1.50% mensal
        double interest = getBalance() * interestRate;
        setBalance(getBalance() + interest);
        addTransaction(new Transaction("Rendimento", interest, this));
        this.accumulatedInterest += interest; // Acumula o rendimento

        // Cobra 1% do rendimento mensal do cliente, mensalmente, caso o rendimento seja positivo.
        if (interest > 0) {
            double fee = interest * 0.01;
            setBalance(getBalance() - fee);
            addTransaction(new Transaction("Taxa de Rendimento", -fee, this));
        }
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


