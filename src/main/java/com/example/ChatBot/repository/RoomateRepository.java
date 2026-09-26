package com.example.ChatBot.repository;

import com.example.ChatBot.entity.Home;
import com.example.ChatBot.entity.Roomate;
import com.example.ChatBot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomateRepository extends JpaRepository<Roomate, String>, JpaSpecificationExecutor<Roomate> {
    boolean existsByUserId(String Id);
}
