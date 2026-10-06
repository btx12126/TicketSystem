package com.example.demo.controllers;

import com.example.demo.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping("/buy")
    public String buy(@RequestParam Integer clientId,@RequestParam Integer ticketId){
        return orderService.buyTicket(clientId,ticketId);
    }
}
