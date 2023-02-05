package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CritereRepository extends JpaRepository<Critere,Long> {
    Critere findCritereById(Long id);

    List<Critere> findCritereByEntretien(Entretien entretien);
}
