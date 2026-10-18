package com.example.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal price;
    private String category;
    private boolean available=true;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private Client owner=null;
    @Version
    private Long version;

    public Ticket(BigDecimal price, String category){
        this.price=price;
        this.category=category;
    }
    public Ticket(){}
    public BigDecimal getPrice(){
        return price;
    }
    public String getCategory(){
        return category;
    }
    public Integer getId(){
        return id;
    }
    public void bought(){
        available=false;
    }
    public boolean getAvailable(){
        return available;
    }
    public void setOwner(Client owner){
        this.owner=owner;
    }
}
