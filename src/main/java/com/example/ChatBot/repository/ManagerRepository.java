package com.example.ChatBot.repository;

import com.example.ChatBot.dto.response.ManagerResponse;
import com.example.ChatBot.entity.Home;
import com.example.ChatBot.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    @Query("""
        SELECT  new com.example.ChatBot.dto.response.ManagerResponse(
             m.home.id,
             h.address,
             h.price,
             h.quantity,
             h.type
            )
        FROM Manager m
        JOIN m.home h
        WHERE m.user.id = :userId 
        AND h.statusDelete = false
    """)
    List<ManagerResponse> findHomesByUserId(@Param("userId") String userId);
    Optional<Manager> findByHome_Id(String idHome);
}
