package com.example.ChatBot.repository;

import com.example.ChatBot.dto.request.CreateHomeRequest;
import com.example.ChatBot.entity.Home;
import com.example.ChatBot.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, String>, JpaSpecificationExecutor<Home> {
    boolean existsByAddressAndType(String address, String type);
    Page<Home> findBystatusDeleteFalse(Pageable pageable);
}
