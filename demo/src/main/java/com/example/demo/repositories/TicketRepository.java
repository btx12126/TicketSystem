package com.example.demo.repositories;

import com.example.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("SELECT a FROM Ticket a WHERE a.available=true")
    List<Ticket> findAvailable();
}
