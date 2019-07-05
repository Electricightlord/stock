package com.lihao.stock;

import com.lihao.stock.listener.ApplicationStartListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockApplication {

	public static void main(String[] args) {
		SpringApplication springApplication=new SpringApplication(StockApplication.class);
		springApplication.addListeners(new ApplicationStartListener());
		springApplication.run(args);
	}

}
