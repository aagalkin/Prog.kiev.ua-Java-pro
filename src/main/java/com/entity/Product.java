package com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "descr")
    private String desc;

    @Column(name = "img")
    private byte[] img;

    @Column(name = "img_type")
    private String imgType;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, Double price, Category category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ProductDTO toDTO(){
        ProductDTO dto = new ProductDTO();
        dto.setName(name);
        dto.setId(id);
        dto.setCategory(category);
        dto.setDesc(desc);
        dto.setPrice(price);
        dto.setImgType(imgType);
        dto.setImg(Base64.encode(img));
        return dto;
    }
}
