package com.example.ChatBot.repository;

import com.example.ChatBot.dto.response.ManagerResponse;
import com.example.ChatBot.dto.response.MyAppointmentResponse;
import com.example.ChatBot.entity.Appointment;
import com.example.ChatBot.entity.Manager;
import com.example.ChatBot.entity.User;
import jakarta.persistence.JoinColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    Optional<Appointment> findByUser(User user);
    List<Appointment> findManagerByUser(@Param("idUser") User user);

    @Query("""
        SELECT ap
        FROM Appointment ap
        JOIN ap.manager ma
        WHERE ma.user.id = :idUser
    """)
    List<Appointment> findAppointmentbyIdLandLord(String idUser);
}

