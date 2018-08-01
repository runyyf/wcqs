package com.yyf.wcqs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.yyf.wcqs.repository")
@EnableScheduling
public class WcqsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WcqsApplication.class, args);
	}
}
