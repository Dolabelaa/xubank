package util;

import model.*;

public class Validator {

    public static boolean isValidName(String name) {
        boolean resp = false;

        if (name != null && !name.trim().isEmpty())
            resp = true;
        else
            System.out.println("Nome inválido.");

        return resp;
    }

    public static boolean isValidCPF(String cpf) {
        boolean resp = false;
        if (cpf != null && cpf.matches("\\d{11}"))
            resp = true;
        else 
            System.out.println("CPF inválido. Deve conter 11 dígitos numéricos.");

        return resp;
    }

    public static boolean isValidPassword(String password) {
        boolean resp = false;

        if (password != null && password.length() >= 4)
            resp = true;
        else
            System.out.println("Senha inválida. Deve conter pelo menos 4 caracteres.");

        return resp;
    }

    public static boolean isValidAmount(String amount) {
        boolean resp = false;

        if (amount != null && amount.matches("\\d+(\\.\\d{1,2})?")) {
            resp = true;
        } else {
            System.out.println("Deve ser um número positivo com até duas casas decimais.");
        }

        return resp;
    }

    public static boolean hasAccountCategory(Client client, String category) {
        boolean resp = client.getAccounts().stream()
            .anyMatch(account -> account.getCategory().equalsIgnoreCase(category));

        if (resp)
            System.out.println("Você já possui uma Conta" + category + ".");

        return resp;
    }
}
