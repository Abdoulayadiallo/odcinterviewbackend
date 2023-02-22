package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Payload.CritereResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CritereRepository extends JpaRepository<Critere,Long> {
    Critere findCritereById(Long id);
    Boolean existsCritereByCritereNom(String nom);

    List<Critere> findCritereByEntretien(Entretien entretien);

    Page<Critere> findCritereByEntretien(Entretien entretien, Pageable pageable);
}
