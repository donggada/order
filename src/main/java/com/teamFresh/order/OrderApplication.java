package com.teamFresh.order;

import com.teamFresh.order.entity.Product;
import com.teamFresh.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class OrderApplication implements ApplicationRunner {
	private final ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments args) {
		// 테스트용
//		productRepository.saveAll(
//				List.of(
//						Product.createProduct("상품A", 100),
//						Product.createProduct("상품B", 50),
//						Product.createProduct("상품C", 20),
//						Product.createProduct("상품D", 5)
//				)
//		);
	}
}
