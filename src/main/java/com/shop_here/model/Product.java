package com.shop_here.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Double price;
    private Integer stock;
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

}
