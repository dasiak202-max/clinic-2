package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping(value = "/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentServ;

    @Autowired
    private AppointmentRepository appointmentRepo;   // used by the query endpoints

    // ===== CRUD =====

    // CREATE  ->  POST /api/appointment/save?patientId=...&doctorId=...
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment,
                                             @RequestParam UUID patientId,
                                             @RequestParam UUID doctorId) {
        String message = appointmentServ.saveAppointment(appointment, patientId, doctorId);
        return respond(message, "Appointment saved successfully", HttpStatus.CREATED);
    }

    // READ all  ->  GET /api/appointment/all
    @GetMapping(value = "/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentServ.getAllAppointments(), HttpStatus.OK);
    }

    // READ one  ->  GET /api/appointment/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id) {
        Appointment appointment = appointmentServ.getAppointmentById(id);
        if (appointment == null) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    // UPDATE  ->  PUT /api/appointment/update/{id}
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
        String message = appointmentServ.updateAppointment(id, appointment);
        return respond(message, "Appointment updated successfully", HttpStatus.OK);
    }

    // DELETE  ->  DELETE /api/appointment/delete/{id}
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id) {
        String message = appointmentServ.deleteAppointment(id);
        return respond(message, "Appointment deleted successfully", HttpStatus.OK);
    }

    // ===== Exercise 4 queries =====

    @GetMapping("/doctor/{doctorId}/pending")
    public List<Appointment> pendingOfDoctor(@PathVariable UUID doctorId) {
        return appointmentRepo.findByDoctorIdAndStatusOrderByAppointmentDateAsc(doctorId, AppointmentStatus.PENDING);
    }

    @GetMapping("/overdue")
    public List<Appointment> overduePending() {
        return appointmentRepo.findOverduePendingAppointments();
    }

    @GetMapping("/count-per-doctor")
    public List<Object[]> countPerDoctor() {
        return appointmentRepo.countAppointmentsPerDoctor();
    }

    @GetMapping("/specialization/{name}")
    public List<Appointment> bySpecialization(@PathVariable String name) {
        return appointmentRepo.findBySpecializationName(name);
    }

    @GetMapping("/doctor/{doctorId}/page")
    public ResponseEntity<?> pageOfDoctor(@PathVariable UUID doctorId,
                                          @RequestParam(defaultValue = "0") int page) {
        Page<Appointment> result = appointmentRepo.findByDoctorId(doctorId,
                PageRequest.of(page, 10, Sort.by("appointmentDate").ascending()));
        return ResponseEntity.ok(Map.of(
                "content", result.getContent(),
                "page", result.getNumber(),
                "size", result.getSize(),
                "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages()));
    }

    // Helper: turns the service's message into the right HTTP status
    private ResponseEntity<String> respond(String message, String successMessage, HttpStatus successStatus) {
        if (message.equals(successMessage)) {
            return new ResponseEntity<>(message, successStatus);
        }
        if (message.endsWith("not found")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
