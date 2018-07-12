package com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    private String name;

    private Double price;

    private String desc;

    private String img;

    private String imgType;

    private Category category;

    public ProductDTO(String name, Double price, Category category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product getKey(){
        Product product = new Product();
        product.setId(id);
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO product = (ProductDTO) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
