package com.cse464;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    @Test
    void negativeWithdrawalMustNeverIncreaseBalance() {
        BankAccount account = new BankAccount(100);
        try {
            account.withdraw(-10);
        } catch (IllegalArgumentException expected) {
            // Rejection is valid; independently verify that state is preserved.
        }
        assertEquals(100, account.getBalance(),
                "A negative withdrawal must never credit the account");
    }

    @Test
    void startsWithInitialBalance() {
        assertEquals(100, new BankAccount(100).getBalance());
        assertEquals(0, new BankAccount(0).getBalance());
    }

    @Test
    void rejectsNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1));
    }

    @Test
    void depositsPositiveAmount() {
        BankAccount account = new BankAccount(100);
        account.deposit(25);
        assertEquals(125, account.getBalance());
    }

    @Test
    void rejectsInvalidDepositsWithoutChangingBalance() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-25));
        assertEquals(100, account.getBalance());
    }

    @Test
    void rejectsDepositOverflowWithoutChangingBalance() {
        BankAccount account = new BankAccount(Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> account.deposit(1));
        assertEquals(Long.MAX_VALUE, account.getBalance());
    }

    @Test
    void withdrawsPositiveAmount() {
        BankAccount account = new BankAccount(100);
        account.withdraw(40);
        assertEquals(60, account.getBalance());
    }

    @Test
    void allowsWithdrawalOfEntireBalance() {
        BankAccount account = new BankAccount(100);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }

    @Test
    void rejectsOverdraftWithoutChangingBalance() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(101));
        assertEquals(100, account.getBalance());
    }

    @Test
    void rejectsZeroWithdrawalWithoutChangingBalance() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertEquals(100, account.getBalance());
    }

    @Test
    void rejectsNegativeWithdrawalWithoutChangingBalance() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-10));
        assertEquals(100, account.getBalance());
    }
}
