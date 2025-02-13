package com.teamFresh.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerAddress;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(String customerName, String customerAddress) {
        this.id = null;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
    }

    public static Order createOrder (String customerName, String customerAddress) {
        return new Order(customerName, customerAddress);
    }
}
