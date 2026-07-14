package com.cpbattle.CPBattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CpBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CpBattleApplication.class, args);
	}

}
