package com.heymart.balance.service;

import com.heymart.balance.exceptions.BalanceNotFoundException;
import com.heymart.balance.model.Balance;
import com.heymart.balance.model.Transaction;
import com.heymart.balance.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceImplTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    @Test
    void createBalance_shouldCreateNewBalance() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Balance.OwnerType ownerType = Balance.OwnerType.USER;
        Balance dummy = new Balance(ownerId, ownerType);
        dummy.setId(id);

        doReturn(Optional.empty()).when(balanceRepository).findByOwnerId(ownerId);
        doReturn(dummy).when(balanceRepository).save(any());

        Balance createdBalance = balanceService.createBalance(ownerId, ownerType).join();

        assertNotNull(createdBalance);
        assertNotNull(createdBalance);
        assertEquals(ownerId, createdBalance.getOwnerId());
        assertEquals(ownerType, createdBalance.getOwnerType());
        assertEquals(0.0, createdBalance.getBalance());

        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @Test
    void createBalance_shouldThrowExceptionIfBalanceExists() {
        UUID ownerId = UUID.randomUUID();
        Balance existingBalance = new Balance(ownerId, Balance.OwnerType.USER);

        doReturn(Optional.of(existingBalance)).when(balanceRepository).findByOwnerId(ownerId);

        assertThrows(IllegalArgumentException.class, () -> balanceService.createBalance(ownerId, Balance.OwnerType.USER).join());
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    void topUp_shouldIncreaseBalanceAndCreateTransaction() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        double initialAmount = 100.0;
        double topUpAmount = 50.0;
        Balance balance = new Balance(ownerId, Balance.OwnerType.USER);
        balance.setBalance(initialAmount);
        balance.setId(id);

        doReturn(Optional.of(balance)).when(balanceRepository).findByOwnerId(ownerId);
        doReturn(balance).when(balanceRepository).save(any());
        doReturn(Optional.of(balance)).when(balanceRepository).findById(id);

        CompletableFuture<Optional<Balance>> result = balanceService.topUp(ownerId, topUpAmount);

        assertTrue(result.join().isPresent());
        Balance updatedBalance = result.join().get();
        assertEquals(initialAmount + topUpAmount, updatedBalance.getBalance());

        verify(balanceRepository, times(1)).save(any(Balance.class));
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void topup_shouldThrowExceptionIfNotFound() {
        UUID ownerId = UUID.randomUUID();
        double withdrawAmount = 100.0;

        when(balanceRepository.findByOwnerId(ownerId)).thenReturn(Optional.empty());

        assertThrows(BalanceNotFoundException.class, () -> balanceService.topUp(ownerId, withdrawAmount).join());

        verify(balanceRepository, never()).save(any(Balance.class));
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void withdraw_shouldDecreaseBalanceAndCreateTransaction() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        double initialAmount = 100.0;
        double withdrawAmount = 50.0;
        Balance balance = new Balance(ownerId, Balance.OwnerType.USER);
        balance.setBalance(initialAmount);
        balance.setId(id);

        doReturn(Optional.of(balance)).when(balanceRepository).findByOwnerId(ownerId);
        doReturn(Optional.of(balance)).when(balanceRepository).findById(id);

        CompletableFuture<Optional<Balance>> result = balanceService.withdraw(ownerId, withdrawAmount);

        assertTrue(result.join().isPresent());
        Balance updatedBalance = result.join().get();
        assertEquals(initialAmount - withdrawAmount, updatedBalance.getBalance());

        verify(balanceRepository, times(1)).save(any(Balance.class));
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    void withdraw_shouldThrowExceptionIfInsufficientFunds() {
        UUID ownerId = UUID.randomUUID();
        double initialAmount = 50.0;
        double withdrawAmount = 100.0;
        Balance balance = new Balance(ownerId, Balance.OwnerType.USER);
        balance.setBalance(initialAmount);

        when(balanceRepository.findByOwnerId(ownerId)).thenReturn(Optional.of(balance));

        assertThrows(IllegalArgumentException.class, () -> balanceService.withdraw(ownerId, withdrawAmount).join());

        verify(balanceRepository, never()).save(any(Balance.class));
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void withdraw_shouldThrowExceptionIfNotFound() {
        UUID ownerId = UUID.randomUUID();
        double withdrawAmount = 100.0;

        when(balanceRepository.findByOwnerId(ownerId)).thenReturn(Optional.empty());

        assertThrows(BalanceNotFoundException.class, () -> balanceService.withdraw(ownerId, withdrawAmount).join());

        verify(balanceRepository, never()).save(any(Balance.class));
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void checkout_shouldProcessTransactionBetweenUserAndSupermarket() {
        UUID userId = UUID.randomUUID();
        UUID supermarketId = UUID.randomUUID();
        double initialUserAmount = 100.0;
        double initialSupermarketAmount = 50.0;
        double checkoutAmount = 40.0;

        Balance userBalance = new Balance(userId, Balance.OwnerType.USER);
        userBalance.setBalance(initialUserAmount);
        Balance supermarketBalance = new Balance(supermarketId, Balance.OwnerType.SUPERMARKET);
        supermarketBalance.setBalance(initialSupermarketAmount);

        doReturn(Optional.of(userBalance)).when(balanceRepository).findByOwnerId(userId);
        doReturn(Optional.of(supermarketBalance)).when(balanceRepository).findByOwnerId(supermarketId);
        doReturn(userBalance).when(balanceRepository).save(userBalance);
        doReturn(supermarketBalance).when(balanceRepository).save(supermarketBalance);

        CompletableFuture<List<Balance>> result = balanceService.checkout(userId, supermarketId, checkoutAmount);

        List<Balance> balances = result.join();
        assertEquals(2, balances.size());
        Balance updatedUserBalance = balances.get(0);
        Balance updatedSupermarketBalance = balances.get(1);

        assertEquals(initialUserAmount - checkoutAmount, updatedUserBalance.getBalance());
        assertEquals(initialSupermarketAmount + checkoutAmount, updatedSupermarketBalance.getBalance());

        verify(balanceRepository, times(6)).findByOwnerId(any(UUID.class)); // Called twice in service methods and twice in reload
        verify(balanceRepository, times(2)).save(any(Balance.class));
        verify(transactionService, times(2)).createTransaction(any(Transaction.class));
    }

    @Test
    void checkout_ShouldThrowErrorIfNotFound() {
        UUID ownerId1 = UUID.randomUUID();
        UUID ownerId2 = UUID.randomUUID();
        double checkoutAmount = 100.0;

        when(balanceRepository.findByOwnerId(ownerId1)).thenReturn(Optional.empty());
        when(balanceRepository.findByOwnerId(ownerId2)).thenReturn(Optional.empty());

        assertThrows(BalanceNotFoundException.class, () -> balanceService.checkout(ownerId1, ownerId2, checkoutAmount).join());

        verify(balanceRepository, never()).save(any(Balance.class));
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Balance.OwnerType ownerType = Balance.OwnerType.USER;
        Balance dummy = new Balance(ownerId, ownerType);
        dummy.setId(id);

        doReturn(Optional.of(dummy))
                .when(balanceRepository).findById(id);
        Optional<Balance> foundBalance = balanceService.findById(id).join();

        assertTrue(foundBalance.isPresent());
        assertEquals(foundBalance.get().getOwnerId(), ownerId);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(balanceRepository).findById(id);
        Optional<Balance> foundBalance = balanceService.findById(id).join();

        assertTrue(foundBalance.isEmpty());
    }

    @Test
    void testFindByOwnerIdFound() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Balance.OwnerType ownerType = Balance.OwnerType.USER;
        Balance dummy = new Balance(ownerId, ownerType);
        dummy.setId(id);

        doReturn(Optional.of(dummy))
                .when(balanceRepository).findById(ownerId);
        Optional<Balance> foundBalance = balanceService.findById(ownerId).join();

        assertTrue(foundBalance.isPresent());
        assertEquals(foundBalance.get().getOwnerId(), ownerId);
    }

    @Test
    void testFindByOwnerIdNotFound() {
        UUID id = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(balanceRepository).findByOwnerId(id);
        Optional<Balance> foundBalance = balanceService.findByOwnerId(id).join();

        assertTrue(foundBalance.isEmpty());
    }

}
