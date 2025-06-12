package controller;

import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;

import model.*;
import util.Validator;

public class ClientController {

    public static void createClient(Bank bank, Scanner sc) {
        String name;
        do {
            System.out.println("Digite seu nome: ");
            name = sc.nextLine();
        } while (!Validator.isValidName(name));

        String cpf;
        do {
            System.out.println("Digite seu CPF: ");
            cpf = sc.nextLine();
        } while (!Validator.isValidCPF(cpf));

        String password;
        do {
            System.out.println("Digite sua senha: ");
            password = sc.nextLine();
        } while (!Validator.isValidPassword(password));

        bank.addClient(new Client(name, cpf, password));

        System.out.println("Conta criada com sucesso!");
    }

    public static boolean chooseOption(int option, Client client, Scanner sc) {
        boolean resp = true;

        switch (option) {
            case 1:
                while (AccountController.chooseAccount(client, sc));
                break;
            case 2:
                AccountController.createAccount(client, sc);
                break;
            case 3:
                viewMonthlyStatement(client);
                break;
            case 4:
                System.out.println("Saindo... Até logo " + client.getName() + "!");
                resp = false;
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                break;
        }

        return resp;
    }

    public static void clientMenu(Client client, Scanner sc) {
        int option = 0;

        do {
            System.out.println("\nBem vindo, " + client.getName() + "!");
            System.out.println(
                "1. Acessar Minhas Contas\n" +
                "2. Criar Nova Conta\n" +
                "3. Ver Extrato do Último Mês\n" +
                "4. Sair"
            );

            option = 0;

            if(sc.hasNextInt())
                option = sc.nextInt();
            sc.nextLine();
        } while (chooseOption(option, client, sc));
    }

    private static void viewMonthlyStatement(Client client) {
        System.out.println("\n--- Extrato do Último Mês para " + client.getName() + " ---");
        if (client.getAccounts().isEmpty()) {
            System.out.println("Nenhuma conta encontrada para este cliente.");
            return;
        }

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        Date oneMonthAgoDate = Date.from(oneMonthAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (Account account : client.getAccounts()) {
            System.out.println("\nConta: " + account.getCategory() + " (Saldo: " + util.Formatter.formatCurrency(account.getBalance()) + ")");
            boolean hasTransactions = false;
            for (Transaction transaction : account.getTransactions()) {
                if (transaction.getDate().after(oneMonthAgoDate)) {
                    System.out.println("  " + util.Formatter.formatDate(transaction.getDate()) + " - " + transaction.getType() + ": " + util.Formatter.formatCurrency(transaction.getAmount()));
                    hasTransactions = true;
                }
            }
            if (!hasTransactions) {
                System.out.println("  Nenhuma transação no último mês.");
            }
        }
        System.out.println("--------------------------------------");
    }
}


