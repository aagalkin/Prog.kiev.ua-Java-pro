package com.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "client")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "city")
    private String city;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client")
    private List<Order> orders;

    @OneToOne
    @JoinColumn(name = "container_id")
    private Container container;

    public Client(String name, String surname, String email, String password, Integer phone, String city, Role role, List<Order> orders){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.orders = orders;
        this.city = city;
    }

    public void addOrder(Order order){
        orders.add(order);
    }
}
