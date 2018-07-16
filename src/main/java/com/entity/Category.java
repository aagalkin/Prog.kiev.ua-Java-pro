package com.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.Translit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "link_name")
    private String linkName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private List<Product> products;

    public void addProduct(Product product){
        products.add(product);
    }

    public Category(String name){
        this.name = name;
        this.linkName = Translit.toTranslit(name);
        this.products = new ArrayList<>();
    }
}
