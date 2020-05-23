package com.stacko.capmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(
		exclude = { RedisRepositoriesAutoConfiguration.class }
		)
public class AshesiCapMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AshesiCapMatchApplication.class, args);
	}

}
