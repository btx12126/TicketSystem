package com.example.demo.services;

import com.example.demo.model.Client;
import com.example.demo.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository repo;


    public ClientService(ClientRepository repo){
        this.repo=repo;
    }

    public String createClient(String name,String surname){
        if (name==null || name.isEmpty()){
            throw new IllegalArgumentException("Wrong name");
        }
        if(surname==null || surname.isEmpty()){
            throw new IllegalArgumentException("Wrong Surname");
        }
        Client client=new Client(name,surname);
        repo.save(client);
        return "client added succesfuly";
    }


}
