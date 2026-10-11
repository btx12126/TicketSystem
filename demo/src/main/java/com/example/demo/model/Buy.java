package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Buy {
    @ManyToOne(optional=false)
    @JoinColumn(name="ticket_id")
    private Ticket ticket;
    @ManyToOne(optional=false)
    @JoinColumn(name="client_id")
    private Client client;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime purchaseDate;

    public Buy(Ticket ticket,Client client, LocalDateTime purchaseDate){
        this.ticket=ticket;
        this.client=client;
        this.purchaseDate=purchaseDate;
    }
    public Buy(){}
    public Ticket getTicket(){
        return ticket;
    }
    public Client getClient(){
        return client;
    }
    public Integer getId(){
        return id;
    }
    public LocalDateTime getPurchaseDate(){
        return purchaseDate;
    }
}
