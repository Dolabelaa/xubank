package controller;

import java.util.Scanner;

import model.Account;
import model.CurrentAccount;
import model.FixedIncomeAccount;
import model.InvestmentAccount;
import model.Transaction;
import util.Validator;

public class TransactionController {
    public static void createTransaction(String type, Account account, Scanner sc) {
        if (type.equals("Depósito")) {
            System.out.println("Digite o valor que deseja depositar (0.00): ");

            System.out.print("R$ ");
            String input = sc.nextLine();

            if (Validator.isValidAmount(input)) {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("O valor do depósito deve ser maior que zero.");
                    return;
                }
                // Lógica de taxa para conta corrente com saldo negativo
                if (account instanceof CurrentAccount && account.getBalance() < 0) {
                    double taxa = (Math.abs(account.getBalance()) * 0.03) + 10.0;
                    amount -= taxa;
                    System.out.printf("Foi aplicada uma taxa de R$ %.2f devido ao saldo negativo.\n", taxa);
                }
                account.deposit(amount);
                System.out.println("Depósito realizado com sucesso!");
            } else {
                System.out.println("Valor inválido.");
            }
        } else if (type.equals("Saque")) {
            System.out.println("Digite o valor que deseja sacar (0.00): ");

            System.out.print("R$ ");
            String input = sc.nextLine();

            if (Validator.isValidAmount(input)) {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("O valor do saque deve ser maior que zero.");
                    return;
                }

                // Lógica de imposto para Renda Fixa e Investimento
                if (account instanceof FixedIncomeAccount || account instanceof InvestmentAccount) {
                    double taxableInterest = account.getTaxableInterest();
                    double taxRate = 0.0;
                    if (account instanceof FixedIncomeAccount) {
                        taxRate = 0.15; // 15% para Renda Fixa
                    } else if (account instanceof InvestmentAccount) {
                        taxRate = 0.225; // 22.5% para Investimento
                    }
                    double tax = taxableInterest * taxRate;
                    System.out.printf("Imposto sobre rendimento (%.1f%%): R$ %.2f\n", taxRate * 100, tax);
                    account.setBalance(account.getBalance() - tax); // Deduz o imposto do saldo
                    account.addTransaction(new Transaction("Imposto sobre Rendimento", -tax, account));
                    account.deductTaxableInterest(taxableInterest); // Zera o rendimento tributável após o saque
                }

                if (account.withdraw(amount)) {
                    System.out.println("Saque realizado com sucesso!");
                } else {
                    System.out.println("Saldo insuficiente para realizar o saque.");
                }
            } else {
                System.out.println("Valor inválido.");
            }
        }
    }
}


