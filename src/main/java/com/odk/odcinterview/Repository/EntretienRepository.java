package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien,Long> {
    Entretien findEntretienById(Long id);
}
