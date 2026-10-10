package com.example.demo.services;

import com.example.demo.model.Ticket;
import com.example.demo.repositories.TicketRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository repo;

    public TicketService(TicketRepository repo){
        this.repo=repo;
    }

    public String addTicket(BigDecimal price, String category){
        if (price<=0){
            throw new IllegalArgumentException("wrong price");
        }
        if(category==null || category.isEmpty()){
            throw new IllegalArgumentException("wrong ticket class");
        }
        Ticket ticket=new Ticket(price,category);
        repo.save(ticket);
        return "ticket added succesfuly";
    }


    public List<Ticket> checkAvailable(){
        return repo.findAvailable();
    }
}
