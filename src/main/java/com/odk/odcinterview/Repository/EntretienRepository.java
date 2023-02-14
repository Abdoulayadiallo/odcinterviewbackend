package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien,Long> {
    Entretien findEntretienById(Long id);
    Page<Entretien> findEntretiensByUtilisateurs(Utilisateur utilisateur, Pageable pageable);
    Page<Entretien> findEntretiensByEntretienNomContaining(String nomentretien, Pageable pageable);

    Entretien findEntretienByPostulants(Postulant postulant);
    // Page<Entretien> findEntretienByParticipantsContaining(Participant participant, Pageable pageable);
}
