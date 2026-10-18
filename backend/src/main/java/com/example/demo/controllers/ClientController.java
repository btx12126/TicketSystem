package com.example.demo.controllers;

import com.example.demo.model.Client;
import com.example.demo.services.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService=clientService;
    }

    @PostMapping("/add-client")
    public Client addClient(@RequestParam String name,@RequestParam String surname){
        return clientService.createClient(name,surname);
    }

}
