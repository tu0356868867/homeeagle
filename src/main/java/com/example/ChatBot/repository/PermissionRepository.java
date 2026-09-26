package com.example.ChatBot.repository;

import com.example.ChatBot.entity.Permission;
import com.example.ChatBot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
