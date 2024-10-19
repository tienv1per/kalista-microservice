package com.theshy.microservices.order.service;

import com.theshy.microservices.order.dto.OrderRequest;
import com.theshy.microservices.order.model.Order;
import com.theshy.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        // map order request to order object
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .price(orderRequest.price())
                .skuCode(orderRequest.skuCode())
                .quantity(orderRequest.quantity())
                .build();
        // save order
        orderRepository.save(order);
    }
}
