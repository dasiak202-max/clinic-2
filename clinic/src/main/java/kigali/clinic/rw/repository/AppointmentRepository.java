package kigali.clinic.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // appointments of a patient by social security number
    List<Appointment> findByPatientSocialSecurityNumber(String socialSecurityNumber);

    // ===== Exercise 4 queries =====

    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(UUID doctorId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a " +
           "WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.PENDING " +
           "AND a.appointmentDate < CURRENT_DATE " +
           "ORDER BY a.appointmentDate ASC")
    List<Appointment> findOverduePendingAppointments();

    @Query("SELECT a.doctor.id, a.doctor.firstName, a.doctor.lastName, COUNT(a) " +
           "FROM Appointment a " +
           "GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT a FROM Appointment a " +
           "JOIN a.doctor d " +
           "JOIN d.specializations s " +
           "WHERE LOWER(s.name) = LOWER(:specName)")
    List<Appointment> findBySpecializationName(@Param("specName") String specName);

    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

    // ===== Quiz Part A =====

    // A2: by status, earliest date first
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    // A3: dates between start and end (both inclusive), ordered by date
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    // A4: does this doctor already have a non-cancelled appointment on that date?
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);

}   // <-- the interface ends HERE: every method must be above this brace