package br.com.app.bancostout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.app.bancostout.service.CostumerService;

@SpringBootTest()
class BancoStoutApplicationTests {
	
	@Autowired
	CostumerService costumerService;
	
	@Test
	void contextLoads() {
		costumerService.createNewCostumer("felipe", "FelipeTM", "123456");
	}

}
