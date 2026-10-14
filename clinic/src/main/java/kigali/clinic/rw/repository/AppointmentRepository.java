package kigali.clinic.rw.repository;

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

    // (from before) appointments of a patient by social security number
    List<Appointment> findByPatientSocialSecurityNumber(String socialSecurityNumber);

    // 1. DERIVED: all appointments of a doctor with a given status, oldest date first
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(UUID doctorId, AppointmentStatus status);

    // 2. JPQL: PENDING appointments whose date has already passed
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.PENDING " +
           "AND a.appointmentDate < CURRENT_DATE " +
           "ORDER BY a.appointmentDate ASC")
    List<Appointment> findOverduePendingAppointments();

    // 3. AGGREGATE: number of appointments per doctor, busiest first
    @Query("SELECT a.doctor.id, a.doctor.firstName, a.doctor.lastName, COUNT(a) " +
           "FROM Appointment a " +
           "GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsPerDoctor();

    // 4. JOIN across 2 relationships: Appointment -> Doctor -> Specialization
    @Query("SELECT a FROM Appointment a " +
           "JOIN a.doctor d " +
           "JOIN d.specializations s " +
           "WHERE LOWER(s.name) = LOWER(:specName)")
    List<Appointment> findBySpecializationName(@Param("specName") String specName);

    // 5. PAGINATION: one page of a doctor's appointments (page size and sorting come from Pageable)
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
}
