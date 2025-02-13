package com.teamFresh.order.entity;

import com.teamFresh.order.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int stock;

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw OutOfStockException.build(id, stock, quantity);
        }
        this.stock -= quantity;
    }

    private Product(Long id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public static Product createProduct(String name, int stock) {
        return new Product(null, name, stock);
    }
}
