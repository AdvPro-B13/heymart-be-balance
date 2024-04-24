package com.heymart.balance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BalanceApplicationTests {

	private List<Balance> balances;
	@BeforeEach
	void setUp() {
		this.balances = new ArrayList<>();
		Balance balance1 = new Balance();
		balance1.setBalanceId('f0c9094c-a1b7-41f7-a444-0410e3ccce04');
		balance1.setBalanceUser('Udin');
		balance1.setBalance(0);
		
		Balance balance2 = new Balance();
		balance2.setBalanceId('e221c49c-0c31-4cb6-a72d-2a732952fd20');
		balance2.setBalanceUser('Petot');
		balance2.setBalance(0);

		this.balances.add(balance1);
		this.balances.add(balance2);
	}

	@Test
	void contextLoads() {
	}

}
