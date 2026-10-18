package com.example.demo.services;

import com.example.demo.model.Buy;
import com.example.demo.model.Client;
import com.example.demo.model.Ticket;
import com.example.demo.repositories.BuyRepository;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final TicketRepository repoTicket;
    private final ClientRepository repoClient;
    private final BuyRepository repoBuy;

    public OrderService(TicketRepository repoTicket, ClientRepository repoClient, BuyRepository repoBuy){
        this.repoTicket=repoTicket;
        this.repoClient=repoClient;
        this.repoBuy=repoBuy;
    }

    @Transactional
    public Buy buyTicket(Integer clientId, Integer ticketId){
        if (clientId==null || ticketId==null){
            throw new IllegalArgumentException("transaction failed. Wrong parametrs");
        }
        Ticket foundTicket=repoTicket.findById(ticketId).orElseThrow(()->new IllegalArgumentException("ticket not found"));
        Client foundClient=repoClient.findById(clientId).orElseThrow(()->new IllegalArgumentException("client not found"));
        if (!foundTicket.getAvailable()){
            throw new IllegalStateException("ticket already sold");
        }
        foundTicket.setOwner(foundClient);
        foundTicket.bought();
        repoTicket.save(foundTicket);
        Buy order = new Buy(foundTicket,foundClient, LocalDateTime.now());
        repoBuy.save(order);
        return order;
    }
}
