package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatRepository extends JpaRepository<Etat,Long> {
    Etat findByStatus(String status);
}
