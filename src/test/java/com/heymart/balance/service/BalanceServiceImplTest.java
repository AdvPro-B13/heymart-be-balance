package com.heymart.balance.service;

import com.heymart.balance.model.Supermarket;
import com.heymart.balance.model.SupermarketBalance;
import com.heymart.balance.model.User;
import com.heymart.balance.model.UserBalance;
import com.heymart.balance.repository.SupermarketBalanceRepository;
import com.heymart.balance.repository.UserBalanceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceImplTest {
    @InjectMocks
    UserBalanceServiceImpl userBalanceService;
    @InjectMocks
    SupermarketBalanceServiceImpl supermarketBalanceService;
    @Mock
    UserBalanceRepository userBalanceRepository;
    @Mock
    SupermarketBalanceRepository supermarketBalanceRepository;

    List<UserBalance> userBalances;
    List<SupermarketBalance> supermarketBalances;

    @BeforeEach
    void setup() {
        userBalances = new ArrayList<>();
        supermarketBalances = new ArrayList<>();

        UserBalance Ub1 = new UserBalance(new User("udin"));
        Ub1.setId(UUID.fromString("5efdf6f4-7aaa-4cc8-a37c-af428d6e4459"));
        Ub1.setBalance(75000.0);

        UserBalance Ub2 = new UserBalance(new User("Keira"));
        Ub2.setId(UUID.fromString("118b9247-f64e-4645-9626-9e68fbd85cb8"));

        userBalances.add(Ub1);
        userBalances.add(Ub2);

        SupermarketBalance Sb1= new SupermarketBalance(new Supermarket("udin mart"));
        Sb1.setId(UUID.fromString("17607619-9663-4af4-a33c-a687d5cbc1e0"));
        Sb1.setBalance(100000.0);

        SupermarketBalance Sb2 = new SupermarketBalance(new Supermarket("Keira Mart"));
        Sb2.setId(UUID.fromString("dc55a721-de62-4e69-be48-1ef6edad02c4"));

        supermarketBalances.add(Sb1);
        supermarketBalances.add(Sb2);
    }

    @Test
    void testCreateBalance() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).save(Ub);
        doReturn(Sb).when(supermarketBalanceRepository).save(Sb);

        UserBalance UbResult = userBalanceService.createBalance(Ub);
        verify(userBalanceRepository, times(1)).save(Ub);
        assertEquals(Ub, UbResult);

        SupermarketBalance SbResult = supermarketBalanceService.createBalance(Sb);
        verify(supermarketBalanceRepository, times(1)).save(Sb);
        assertEquals(Sb, SbResult);
    }

    @Test
    void testCreateBalanceIfAlreadyExists() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).findById(Ub.getId());
        doReturn(Sb).when(supermarketBalanceRepository).findById(Sb.getId());

        assertNull(userBalanceService.createBalance(Ub));
        verify(userBalanceRepository, times(0)).save(Ub);

        assertNull(supermarketBalanceService.createBalance(Sb));
        verify(supermarketBalanceRepository, times(0)).save(Sb);   
    }

    @Test
    void testCreateBalanceIfOwnerAlreadyHasBalance() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).findByUserId(Ub.getOwner().getId());
        doReturn(Sb).when(supermarketBalanceRepository).findBySupermarketId(Sb.getOwner().getId());

        assertNull(userBalanceService.createBalance(Ub));
        verify(userBalanceRepository, times(0)).save(Ub);

        assertNull(supermarketBalanceService.createBalance(Sb));
        verify(supermarketBalanceRepository, times(0)).save(Sb);
    }

    @Test
    void testFindByIdFound() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).findById(Ub.getId());
        doReturn(Sb).when(supermarketBalanceRepository).findById(Sb.getId());

        UserBalance UbResult = userBalanceService.findById(Ub.getId());
        assertEquals(Ub, UbResult);

        SupermarketBalance SbResult = supermarketBalanceService.findById(Sb.getId());
        assertEquals(Sb, SbResult);
    }

    @Test
    void testFindByIdNotFound() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(null).when(userBalanceRepository).findById(Ub.getId());
        doReturn(null).when(supermarketBalanceRepository).findById(Sb.getId());

        assertNull(userBalanceService.findById(Ub.getId()));
        assertNull(supermarketBalanceService.findById(Sb.getId()));
    }

    @Test
    void testFindByOwnerIdFound() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).findByUserId(Ub.getOwner().getId());
        doReturn(Sb).when(supermarketBalanceRepository).findBySupermarketId(Sb.getOwner().getId());

        UserBalance UbResult = userBalanceService.findByUserId(Ub.getOwner().getId());
        assertEquals(Ub, UbResult);

        SupermarketBalance SbResult = supermarketBalanceService.findBySupermarketId(Sb.getOwner().getId());
        assertEquals(Sb, SbResult);
    }

    @Test
    void testFindByOwnerIdNotFound() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(null).when(userBalanceRepository).findByUserId(Ub.getOwner().getId());
        doReturn(null).when(supermarketBalanceRepository).findBySupermarketId(Sb.getOwner().getId());

        assertNull(userBalanceService.findByUserId(Ub.getOwner().getId()));
        assertNull(supermarketBalanceService.findBySupermarketId(Sb.getOwner().getId()));
    }
    @Test
    void testTopupSuccess() {
        UserBalance Ub = userBalances.get(1);
        SupermarketBalance Sb = supermarketBalances.get(1);

        doReturn(Ub).when(userBalanceRepository).findById(Ub.getId());
        doReturn(Sb).when(supermarketBalanceRepository).findById(Sb.getId());

        UserBalance UbResult = userBalanceService.topup(Ub, 5000.0);
        assertEquals(5000.0, UbResult.getBalance());

        SupermarketBalance SbResult = supermarketBalanceService.topup(Sb, 7500.0);
        assertEquals(7500.0, SbResult.getBalance());
    }

    @Test
    void testWithdrawSuccess() {
        UserBalance Ub = userBalances.get(0); // balance 75000
        SupermarketBalance Sb = supermarketBalances.get(0); // balance 100000

        doReturn(Ub).when(userBalanceRepository).findById(Ub.getId());
        doReturn(Sb).when(supermarketBalanceRepository).findById(Sb.getId());

        UserBalance UbResult = userBalanceService.withdraw(Ub, 5000.0);
        assertEquals(70000.0, UbResult.getBalance());

        SupermarketBalance SbResult = supermarketBalanceService.withdraw(Sb, 75000.0);
        assertEquals(25000.0, SbResult.getBalance());
    }

    @Test
    void testwithdrawFail() {
        UserBalance Ub = userBalances.get(0);
        Ub.setBalance(10000.0);
        SupermarketBalance Sb = supermarketBalances.get(0);
        Sb.setBalance(10000.0);

        doReturn(Ub).when(userBalanceRepository).findById(Ub.getId());
        doReturn(Sb).when(supermarketBalanceRepository).findById(Sb.getId());

        UserBalance UbResult = userBalanceService.withdraw(Ub, 50000.0);
        assertNull(UbResult);
        assertEquals(10000.0, Ub.getBalance());

        SupermarketBalance SbResult = supermarketBalanceService.withdraw(Sb, 75000.0);
        assertNull(SbResult);
        assertEquals(10000.0, Sb.getBalance());
    }
}