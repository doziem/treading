package com.doziem.treading.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double quantity;

    @ManyToOne
    private Coin coin;

    private Double buyPrice;

    private Double sellPrice;

    @JsonIgnore
    @OneToOne
    private Order order;

}
