package test.java.controller;

import controller.TransactionController;
import model.Account;
import model.Client;
import model.CurrentAccount;
import model.SavingsAccount;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client("Test Client", "12345678900", "password");
    }

    @Test
    @DisplayName("Deve realizar um depósito com sucesso em conta corrente")
    void shouldDepositSuccessfullyIntoCurrentAccount() {
        Account account = new CurrentAccount(testClient);
        account.setBalance(100.0);
        String input = "50.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Depósito", account, scanner);

        assertEquals(150.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
        assertEquals("Depósito", account.getTransactions().get(0).getType());
        assertEquals(50.0, account.getTransactions().get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("Deve realizar um depósito com sucesso em conta poupança")
    void shouldDepositSuccessfullyIntoSavingsAccount() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(200.0);
        String input = "75.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Depósito", account, scanner);

        assertEquals(275.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
        assertEquals("Depósito", account.getTransactions().get(0).getType());
        assertEquals(75.0, account.getTransactions().get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("Deve aplicar taxa ao depositar em conta corrente com saldo negativo")
    void shouldApplyFeeOnDepositWithNegativeBalance() {
        CurrentAccount account = new CurrentAccount(testClient);
        account.setBalance(-50.0);
        String input = "100.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Depósito", account, scanner);

        // Saldo inicial: -50.0
        // Depósito: 100.0
        // Taxa: (50.0 * 0.03) + 10.0 = 1.5 + 10.0 = 11.5
        // Valor líquido depositado: 100.0 - 11.5 = 88.5
        // Saldo final esperado: -50.0 + 88.5 = 38.5
        assertEquals(38.5, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
        assertEquals("Depósito", account.getTransactions().get(0).getType());
        assertEquals(88.5, account.getTransactions().get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("Não deve depositar valor inválido")
    void shouldNotDepositInvalidAmount() {
        Account account = new CurrentAccount(testClient);
        account.setBalance(100.0);
        String input = "abc\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Depósito", account, scanner);

        assertEquals(100.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }

    @Test
    @DisplayName("Deve realizar um saque com sucesso em conta corrente")
    void shouldWithdrawSuccessfullyFromCurrentAccount() {
        CurrentAccount account = new CurrentAccount(testClient);
        account.setBalance(200.0);
        account.setCreditLimit(100.0);
        String input = "250.00\n"; // Saque usando limite de crédito
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(-50.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
        assertEquals("Saque", account.getTransactions().get(0).getType());
        assertEquals(250.0, account.getTransactions().get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("Deve realizar um saque com sucesso em conta poupança")
    void shouldWithdrawSuccessfullyFromSavingsAccount() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(300.0);
        String input = "100.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(200.0, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactions().size());
        assertEquals("Saque", account.getTransactions().get(0).getType());
        assertEquals(100.0, account.getTransactions().get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("Não deve sacar com saldo insuficiente em conta poupança")
    void shouldNotWithdrawWithInsufficientBalanceSavingsAccount() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(50.0);
        String input = "100.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(50.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }

    @Test
    @DisplayName("Não deve sacar com saldo insuficiente em conta corrente")
    void shouldNotWithdrawWithInsufficientBalanceCurrentAccount() {
        CurrentAccount account = new CurrentAccount(testClient);
        account.setBalance(50.0);
        account.setCreditLimit(0.0); // Sem limite de crédito para este teste
        String input = "100.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(50.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }

    @Test
    @DisplayName("Não deve sacar valor inválido")
    void shouldNotWithdrawInvalidAmount() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(100.0);
        String input = "xyz\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(100.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }

    @Test
    @DisplayName("Deve lidar com saque de valor zero")
    void shouldHandleZeroWithdrawal() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(100.0);
        String input = "0.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Saque", account, scanner);

        assertEquals(100.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }

    @Test
    @DisplayName("Deve lidar com depósito de valor zero")
    void shouldHandleZeroDeposit() {
        Account account = new SavingsAccount(testClient);
        account.setBalance(100.0);
        String input = "0.00\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        TransactionController.createTransaction("Depósito", account, scanner);

        assertEquals(100.0, account.getBalance(), 0.001); // Saldo não deve mudar
        assertEquals(0, account.getTransactions().size()); // Nenhuma transação deve ser adicionada
    }
}


