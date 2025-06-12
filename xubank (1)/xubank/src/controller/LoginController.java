package controller;

import java.util.*;

import model.*;

public class LoginController {
    public static Client login(Bank bank, Scanner sc) {
        System.out.println("Digite seu CPF: ");
        String cpf = sc.nextLine();

        Client client = bank.getClients().stream()
            .filter(c -> c.getCpf().equals(cpf))
            .findFirst()
            .orElse(null);

        if (client == null) {
            System.out.println("Cliente não encontrado.");
            return null;
        }

        System.out.println("Digite sua senha: ");
        String password = sc.nextLine();

        if (!client.getPassword().equals(password)) {
            System.out.println("Senha inválida.");
            return null;
        }

        System.out.println("Login realizado com sucesso!");
        return client;
    }
}