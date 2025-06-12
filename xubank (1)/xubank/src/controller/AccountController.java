package controller;

import java.util.*;
import java.util.stream.Collectors;

import model.*;
import util.Validator;
import util.Formatter;

public class AccountController {
    public static void createAccount(Client client, Scanner sc) {
        System.out.println("\nQual tipo de conta deseja criar?");
        System.out.println(
            "1. Corrente\n" +
            "2. Poupança\n" +
            "3. Renda Fixa\n" +
            "4. Investimento"
        );
        int accountType = 0;
        if (sc.hasNextInt())
            accountType = sc.nextInt();
        sc.nextLine();

        switch (accountType) {
            case 1:
                if (!Validator.hasAccountCategory(client, "Corrente")) {
                    client.addAccount(new CurrentAccount(client));
                    System.out.println("Conta Corrente criada com sucesso!");
                }
                break;
            case 2:
                if (!Validator.hasAccountCategory(client, "Poupança")) {
                    client.addAccount(new SavingsAccount(client));
                    System.out.println("Conta Poupança criada com sucesso!");
                }
                break;
            case 3:
                if (!Validator.hasAccountCategory(client, "Renda Fixa")) {
                    client.addAccount(new FixedIncomeAccount(client));
                    System.out.println("Conta Renda Fixa criada com sucesso!");
                }
                break;
            case 4:
                if (!Validator.hasAccountCategory(client, "Investimento")) {
                    client.addAccount(new InvestmentAccount(client));
                    System.out.println("Conta Investimento criada com sucesso!");
                }
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                break;
        }
    }

    public static void getStatement(Account account) {
        if (account.getTransactions().isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }

        System.out.println("\nExtrato: ");

        Date now = new Date();
        Date oneMonthAgo = Date.from(now.toInstant().minusSeconds(30L * 24 * 60 * 60));


        List<Transaction> recentTransactions = account.getTransactions().stream()
            .filter(t -> !t.getDate().before(oneMonthAgo))
            .sorted(Comparator.comparing(Transaction::getDate).reversed()) 
            .collect(Collectors.toList());

        if (recentTransactions.isEmpty()) {
            System.out.println("Nenhuma transação nos últimos 30 dias.");
            return;
        }

        for (Transaction transaction : recentTransactions) {
            System.out.println(
                transaction.getType() + ": " +
                Formatter.formatCurrency(transaction.getAmount()) + " - " +
                Formatter.formatDate(transaction.getDate())
            );
        }
    }

    public static boolean chooseOption(int option, Account account, Scanner sc) {
        boolean resp = true;

        switch (option) {
            case 1:
                TransactionController.createTransaction("Depósito", account, sc);
                break;
            case 2:
                TransactionController.createTransaction("Saque", account, sc);
                break;
            case 3:
                getStatement(account);
                break;
            case 4:
                resp = false;
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                break;
        }

        return resp;
    }

    public static void accountMenu(Account account, Scanner sc) {
        int option = 0;

        do {
            System.out.println("\nConta " + account.getCategory());
            System.out.println("Saldo: " + Formatter.formatCurrency(account.getBalance()));
            System.out.println(
                "\n1. Depositar\n" +
                "2. Sacar\n" +
                "3. Ver Extrato\n" +
                "4. Sair"
            );

            option = 0;

            if(sc.hasNextInt())
                option = sc.nextInt();
            sc.nextLine();
        } while (chooseOption(option, account, sc));
    }

    public static boolean chooseAccount(Client client, Scanner sc) {
        boolean resp = true;

        if (client.getAccounts().isEmpty()) {
            System.out.println("Nenhuma conta disponível. Crie uma conta primeiro.");
            return false;
        }
        System.out.println("\nSelecione uma conta:");
        for (int i = 0; i < client.getAccounts().size(); i++) {
            System.out.println((i + 1) + ". " + client.getAccounts().get(i).getCategory());
        }
        System.out.println((client.getAccounts().size() + 1) + ". Sair");

        int choice = 0;
        if (sc.hasNextInt())
            choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > client.getAccounts().size() + 1) {
            System.out.println("Opção inválida. Tente novamente.");
            return resp;
        }

        if (choice == client.getAccounts().size() + 1) {
            resp = false;
            return resp;
        }

        Account account = client.getAccounts().get(choice - 1);

        accountMenu(account, sc);

        return resp;
    }
}


