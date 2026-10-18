package com.example.demo.controllers;

import com.example.demo.model.Ticket;
import com.example.demo.services.TicketService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService=ticketService;
    }

    @GetMapping("/check-available")
    public List<Ticket> checkAvailable(){
        return ticketService.checkAvailable();
        }

    @PostMapping("/add-ticket")
    public Ticket addTicket(@RequestParam BigDecimal price, @RequestParam String category){
        return ticketService.addTicket(price,category);
    }
}
