package com.example.ChatBot.service;

import com.example.ChatBot.dto.request.AppointmentRequest;
import com.example.ChatBot.dto.request.HomeStatusRequest;
import com.example.ChatBot.dto.response.AppointmentResponse;
import com.example.ChatBot.dto.response.LandlordAppointmentResponse;
import com.example.ChatBot.dto.response.MyAppointmentResponse;
import com.example.ChatBot.dto.response.RenterviewHomeResponse;
import com.example.ChatBot.entity.Appointment;
import com.example.ChatBot.entity.Manager;
import com.example.ChatBot.entity.User;
import com.example.ChatBot.exception.AppException;
import com.example.ChatBot.exception.ErrorCode;
import com.example.ChatBot.repository.AppointmentRepository;
import com.example.ChatBot.repository.ManagerRepository;
import com.example.ChatBot.repository.UserRepository;
import com.example.ChatBot.status.AppointmentStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {
    ManagerRepository managerRepository;
    UserRepository userRepository;
    AppointmentRepository appointmentRepository;
    public AppointmentResponse createAppointment(AppointmentRequest request, String idHome, String idUser) {
        Manager manager = managerRepository.findByHome_Id(idHome)
                .orElseThrow(() ->
                        new AppException(ErrorCode.HOME_NOT_EXISTED)
                );

        User user = userRepository.findById(idUser)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_EXIST)
                );



        LocalDate appointmentDate = request.getAppointmentDate();

        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Không thể đặt lịch trong quá khứ"
            );
        }

        AppointmentResponse response = AppointmentResponse.builder()
                .contact(manager.getHome().getContact())
                .type(manager.getHome().getType())
                .address(manager.getHome().getAddress())
                .appointmentDate(appointmentDate)
                .build();

        Appointment appointment = Appointment.builder()
                .user(user)
                .manager(manager)
                .createdAt(appointmentDate)
                .renterName(user.getFullname())
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);
        return response;
    }

    public List<MyAppointmentResponse> getAppointment(String idUser){
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new AppException(ErrorCode.HOME_NOT_CHOSE)
        );
        List<Appointment> appointment = appointmentRepository.findManagerByUser(user);

        List<MyAppointmentResponse> response = new ArrayList<>();

        for (Appointment item : appointment) {
            if(!item.getManager().getHome().isStatusDelete()){
                MyAppointmentResponse appointmentResponse = MyAppointmentResponse.builder()
                    .address(item.getManager().getHome().getAddress())
                    .createdAt(item.getCreatedAt())
                    .status(item.getStatus())
                    .idAppointment(item.getId())
                    .build();
                 response.add(appointmentResponse);
         }
        }
        return response;
    }

    public List<LandlordAppointmentResponse> getLAResponse(String idUser){
        List<Appointment> appointment = appointmentRepository.findAppointmentbyIdLandLord(idUser);

        List<LandlordAppointmentResponse> response = new ArrayList<>();

        for (Appointment item : appointment) {
            LandlordAppointmentResponse laResponse = LandlordAppointmentResponse.builder()
                    .id(item.getId())
                    .type(item.getManager().getHome().getType())
                    .address(item.getManager().getHome().getAddress())
                    .fullname(item.getRenterName())
                    .appointmentDate(item.getCreatedAt())
                    .status(item.getStatus())
                    .build();

            response.add(laResponse);
        }
        return response;

    }

    public void deleteAppointment(String idUser, String idAppointment){
        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() ->
                        new AppException(ErrorCode.INVALID_DELETE)
                );

        // Kiểm tra phòng có thuộc user hiện tại không
        if (!appointment.getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.INVALID_DELETE);
        }

        appointmentRepository.delete(appointment);
    }

    public RenterviewHomeResponse getAppointmentbyId(String idUser, String idAppointment){
        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() ->
                        new AppException(ErrorCode.INVALID_ACCESS)
                );

        // Kiểm tra tài nguyên có thuộc user hiện tại không
        if (!appointment.getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.INVALID_ACCESS);
        }

        return RenterviewHomeResponse.builder()
                .picture(appointment.getManager().getHome().getPicture())
                .type(appointment.getManager().getHome().getType())
                .contact(appointment.getManager().getHome().getContact())
                .createdAt(appointment.getCreatedAt())
                .address(appointment.getManager().getHome().getAddress())
                .build();
    }

    public void updateStatus(String idUser, String idAppointment, HomeStatusRequest request){
        Appointment appointment = appointmentRepository.findById(idAppointment)
                .orElseThrow(() ->
                        new AppException(ErrorCode.RESOURCES_NOT_AVAILABLE)
                );

        // Kiểm tra tài nguyên có thuộc user hiện tại không
        if (!appointment.getManager().getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.INVALID_ACCESS);
        }

        appointment.setStatus(request.getStatus());

        appointmentRepository.save(appointment);
    }

}


