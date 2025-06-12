import java.util.*;

import model.*;
import controller.*;

public class Main {

    private static Bank bank;
    private static Scanner sc;
    private static Client client;

    public static boolean chooseOption(int option) {
        boolean resp = true;

        switch (option) {
            case 1:
                client = LoginController.login(bank, sc);

                if (client != null) ClientController.clientMenu(client, sc);
                else System.out.println("Falha ao entrar. Tente novamente.");
                break;
            case 2:
                ClientController.createClient(bank, sc);
                break;
            case 3:
                ReportController.generateReport(bank);
                break;
            case 4:
                bank.applyMonthlyInterestsToAllAccounts();
                System.out.println("Rendimentos mensais aplicados a todas as contas.");
                break;
            case 5:
                System.out.println("Saindo da aplicação. Até logo!");
                resp = false;
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                break;
        }

        return resp;
    }

    public static void main(String[] args) {
        sc = new Scanner(System.in);

        bank = new Bank("XuBank");

        int option = 0;

        try {
            do {
                System.out.println("\n" + bank.getName() + " Menu: ");
                System.out.println(
                    "1. Entrar\n" + 
                    "2. Registrar\n" +
                    "3. Relatório\n" + 
                    "4. Aplicar Rendimentos Mensais\n" +
                    "5. Sair"
                );

                option = 0;

                if(sc.hasNextInt())
                    option = sc.nextInt();
                sc.nextLine();

            } while (chooseOption(option));
        } finally {
            sc.close();
        }
    }
}

