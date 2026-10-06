package com.example.demo.controllers;

import com.example.demo.model.Client;
import com.example.demo.model.Ticket;
import com.example.demo.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService order;
    public OrderController(OrderService order){
        this.order=order;
    }
    @PostMapping("/buy")
    public String buy(@RequestParam Integer clientId,@RequestParam Integer ticketId){
        return order.buyTicket(clientId,ticketId);
    }
}
