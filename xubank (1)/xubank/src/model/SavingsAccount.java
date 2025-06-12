package model;

public class SavingsAccount extends Account {

    public SavingsAccount(Client client) {
        super("Poupança", client);
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = getBalance() * 0.0060; // 0.60% mensal
        setBalance(getBalance() + interest);
        addTransaction(new Transaction("Rendimento", interest, this));
    }

    @Override
    public double calculateWithdrawalLimit() {
        return getBalance();
    }

    @Override
    public double getTaxableInterest() {
        return 0.0;
    }

    @Override
    public void deductTaxableInterest(double amount) {
        // Não aplicável para conta poupança
    }
}


