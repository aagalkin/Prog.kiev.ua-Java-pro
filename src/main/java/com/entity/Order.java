package com.entity;

import com.service.WarehouseService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.OverridesAttribute;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "my_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToMany
    @JoinTable(name = "order_product", joinColumns = {@JoinColumn(name = "order_id")}, inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private List<ProductCount> products;

    @Embedded
    private Address delivery;

    public Order(Client client, Double totalPrice, List<ProductCount> products){
        this.client = client;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public void cleanProducts(WarehouseService warehouseService){
        Long[] products_id = new Long[products.size()];
        Integer[] counts = new Integer[products.size()];
        for (int i = 0; i < products.size(); i++){
            products_id[i] = products.get(i).getProduct().getId();
            counts[i] = products.get(i).getCount();
        }
        warehouseService.income(products_id, counts);
    }
}
