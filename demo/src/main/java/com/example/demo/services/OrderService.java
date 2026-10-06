package com.example.demo.services;

import com.example.demo.model.Client;
import com.example.demo.model.Ticket;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final TicketRepository repoTicket;
    private final ClientRepository repoClient;

    public OrderService(TicketRepository repoTicket, ClientRepository repoClient){
        this.repoTicket=repoTicket;
        this.repoClient=repoClient;
    }

    @Transactional
    public String buyTicket(Client client, Ticket ticket){
        Integer clientId=client.getId();
        Integer ticketId=ticket.getId();
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
        return "transaction succesfull";
    }
}
