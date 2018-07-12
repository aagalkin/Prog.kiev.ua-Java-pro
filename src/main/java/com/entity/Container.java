package com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "container")
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product")
    @Column(name = "count")
    @CollectionTable(name = "container_values", joinColumns = {@JoinColumn(name = "container_id")})
    private Map<Product, Integer> products;

    @Column(name = "count")
    private Integer count;

    public void add(Product product, Integer count){
        products.put(product, count);
    }

    public void clear(){
        products.clear();
        count = 0;
    }

    public Integer get(Product product){
        return products.get(product);
    }

    public boolean contains(Product product){
        return products.containsKey(product);
    }

    public Double getSumm(){
        Double summ = 0d;
        for (Map.Entry<Product, Integer> pair : products.entrySet()){
            summ += pair.getKey().getPrice() * pair.getValue();
        }
        return summ;
    }

    public boolean remove(Product product){
        return products.remove(product) != null;
    }
}
