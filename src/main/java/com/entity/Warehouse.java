package com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product")
    @Column(name = "count")
    @CollectionTable(name = "wh", joinColumns = {@JoinColumn(name = "wh_id")})
    private Map<Product, Integer> products;

    public void addProduct(Product product) {
        products.put(product, 0);
    }
}