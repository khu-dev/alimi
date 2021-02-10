package com.khumu.alimi;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AlimiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlimiApplication.class, args);
	}

	@Bean
	public Gson getGson(){
		System.out.println("Gson");
		return new Gson();
	}
}
