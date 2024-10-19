package com.theshy.microservices.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "t_inventory")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
