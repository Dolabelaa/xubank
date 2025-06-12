package controller;

import java.util.Comparator;

import model.*;
import util.Formatter;

public class ReportController {
    public static void generateReport(Bank bank) {
        System.out.println("\nRelatório do Banco: " + bank.getName());

        if (bank.getClients().isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("\nValor atual em custódia para cada tipo de conta");

        System.out.print("Corrente: ");
        double totalCorrente = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .filter(account -> account instanceof CurrentAccount)
            .mapToDouble(Account::getBalance)
            .sum();
        System.out.println(Formatter.formatCurrency(totalCorrente));

        System.out.print("Poupança: ");
        double totalPoupanca = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .filter(account -> account instanceof SavingsAccount)
            .mapToDouble(Account::getBalance)
            .sum();
        System.out.println(Formatter.formatCurrency(totalPoupanca));

        System.out.print("Renda Fixa: ");
        double totalRendaFixa = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .filter(account -> account instanceof FixedIncomeAccount)
            .mapToDouble(Account::getBalance)
            .sum();
        System.out.println(Formatter.formatCurrency(totalRendaFixa));

        System.out.print("Investimento: ");
        double totalInvestimento = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .filter(account -> account instanceof InvestmentAccount)
            .mapToDouble(Account::getBalance)
            .sum();
        System.out.println(Formatter.formatCurrency(totalInvestimento));
        System.out.println("Total em custódia: " + Formatter.formatCurrency(
            totalCorrente + totalPoupanca + totalRendaFixa + totalInvestimento
        ));

        System.out.print("\nSaldo médio das contas: ");
        double totalSaldo = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .mapToDouble(Account::getBalance)
            .sum();
        long totalAccounts = bank.getClients().stream()
            .flatMap(client -> client.getAccounts().stream())
            .count();
        double saldoMedio = totalAccounts > 0 ? totalSaldo / totalAccounts : 0.0;
        System.out.println(Formatter.formatCurrency(saldoMedio));

        Client richestClient = bank.getClients().stream()
            .max(Comparator.comparingDouble(client -> client.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum()))
            .orElse(null);

        Client poorestClient = bank.getClients().stream()
            .min(Comparator.comparingDouble(client -> client.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum()))
            .orElse(null);

        if (richestClient != null) {
            System.out.println("\nCliente mais rico: " + richestClient.getName() + 
                " - Saldo total: " + Formatter.formatCurrency(
                    richestClient.getAccounts().stream()
                        .mapToDouble(Account::getBalance)
                        .sum()
                )
            );
        } else {
            System.out.println("Nenhum cliente encontrado.");
        }

        if (poorestClient != null) {
            System.out.println("Cliente mais pobre: " + poorestClient.getName() + 
                " - Saldo total: " + Formatter.formatCurrency(
                    poorestClient.getAccounts().stream()
                        .mapToDouble(Account::getBalance)
                        .sum()
                )
            );
        } else {
            System.out.println("Nenhum cliente encontrado.");
        }
    }
}

