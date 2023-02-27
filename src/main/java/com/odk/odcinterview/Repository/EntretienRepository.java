package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Etat;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien,Long> {
    Entretien findEntretienById(Long id);
    List<Entretien> findEntretiensByEtat(Etat etat);
    Page<Entretien> findEntretiensByUtilisateurs(Utilisateur utilisateur, Pageable pageable);
    Page<Entretien> findEntretiensByEntretienNomContaining(String nomentretien, Pageable pageable);

    Entretien findEntretienByPostulants(Postulant postulant);
    // Page<Entretien> findEntretienByParticipantsContaining(Participant participant, Pageable pageable);
    @Query(value = "SELECT DISTINCT entretien.* FROM entretien,utilisateur WHERE entretien.entretien_nom like %:keyword% OR entretien.description like %:keyword%", nativeQuery = true)
    Page<Entretien> findEntretienByKeyword(@RequestParam(value = "keyword", required = false) String keyword, Pageable pageable);
}
