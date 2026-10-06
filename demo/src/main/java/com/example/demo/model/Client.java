package com.example.demo.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    @OneToMany(mappedBy="owner")
    List<Ticket> bought=new ArrayList<>();

    public Client(){}
    public Client(String name, String surname){
        this.name=name;
        this.surname=surname;
    }
    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getSurname(){
        return surname;
    }
    public List<Ticket> getBought(){
        return bought;
    }
}
