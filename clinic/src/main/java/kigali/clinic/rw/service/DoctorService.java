package kigali.clinic.rw.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeRepository officeRepo;   // needed to find the office to attach

    @Autowired
    private SpecializationRepository specRepo;

    // CREATE
    public String saveDoctor(Doctor doctor, UUID officeId) {
        if (officeId != null) {
            Office office = officeRepo.findById(officeId).orElse(null);
            if (office == null) {
                return "Office not found";
            }
            if (office.getDoctor() != null) {          
                return "Office already assigned to another doctor";
            }
            doctor.setOffice(office);
        }
        doctorRepo.save(doctor);
        return "Doctor saved successfully";
    }

    // READ all
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

        // B1
    public List<Doctor> getDoctorsBySpecialization(String name) {
        return doctorRepo.findBySpecializationName(name);
    }

        // B2
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

    // READ one
    public Doctor getDoctorById(UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }

    // UPDATE (officeId is optional: only sent if you want to move the doctor)
    public String updateDoctor(UUID id, Doctor newData, UUID officeId) {
        Doctor existing = doctorRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Doctor not found";
        }
        if (officeId != null) {
            Office office = officeRepo.findById(officeId).orElse(null);
            if (office == null) {
                return "Office not found";
            }
            if (office.getDoctor() != null && !office.getDoctor().getId().equals(id)) {
                return "Office already assigned to another doctor";
            }
            existing.setOffice(office);
        }
        existing.setFirstName(newData.getFirstName());
        existing.setLastName(newData.getLastName());
        existing.setDateOfBirth(newData.getDateOfBirth());
        doctorRepo.save(existing);
        return "Doctor updated successfully";
    }
    
    // Quiz 2 - A1: Add specialization to doctor
    @Transactional
    public String addSpecializationToDoctor(
            UUID doctorId, UUID specializationId) {

        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);

        if (doctor == null) {
            return "The doctor with that id does not exist";
        }

        Specialization spec = specRepo.findById(specializationId)
                .orElse(null);

        if (spec == null) {
            return "That specialization does not exist";
        }

        boolean alreadyLinked = doctor.getSpecializations()
                .stream()
                .anyMatch(s -> s.getId().equals(specializationId));

        if (alreadyLinked) {
            return "The doctor already has that specialization";
        }

        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);

        return "Specialization added to the doctor";
    }


    // DELETE
    @Transactional
    public String deleteDoctor(UUID id) {
        Doctor existing = doctorRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Doctor not found";
        }
        // appointment.doctor_id points here, so the database would refuse
        if (!existing.getAppointments().isEmpty()) {
            return "Doctor has appointments and cannot be deleted";
        }
        // remove the doctor's rows from the doctor_specialization join table
        for (Specialization s : existing.getSpecializations()) {
            s.getDoctors().remove(existing);
        }
        doctorRepo.delete(existing);
        return "Doctor deleted successfully";
    }
}
