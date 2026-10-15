
package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    @Query("SELECT o FROM Office o WHERE o.officeNumber = :number")
    Office findByOfficeNumber(@Param("number") int offNumber);

    @Query("SELECT o FROM Office o WHERE o.name = :name")
    List<Office> findByName(@Param("name") String name);
}

