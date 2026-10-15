package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    // CREATE: an appointment needs an existing patient AND doctor
    public String saveAppointment(Appointment appointment, UUID patientId, UUID doctorId) {
        Patient patient = patientRepo.findById(patientId).orElse(null);
        if (patient == null) {
            return "Patient not found";
        }
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) {
            return "Doctor not found";
        }
        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.PENDING);   // default status
        }
                
        if (appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                doctorId, appointment.getAppointmentDate(), AppointmentStatus.CANCELLED)) {
            return "Doctor is already booked on that date";
        }
        
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointmentRepo.save(appointment);
        return "Appointment saved successfully";
    }

    // READ all
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    // READ one
    public Appointment getAppointmentById(UUID id) {
        return appointmentRepo.findById(id).orElse(null);
    }

    // UPDATE: date, reason and status
    public String updateAppointment(UUID id, Appointment newData) {
        Appointment existing = appointmentRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Appointment not found";
        }
        existing.setAppointmentDate(newData.getAppointmentDate());
        existing.setReason(newData.getReason());
        if (newData.getStatus() != null) {
            existing.setStatus(newData.getStatus());
        }
        appointmentRepo.save(existing);
        return "Appointment updated successfully";
    }

    // DELETE: nothing points to an appointment, so it can be deleted directly
    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            return "Appointment not found";
        }
        appointmentRepo.deleteById(id);
        return "Appointment deleted successfully";
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start,end);
    } 

}