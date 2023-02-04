package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    List<Participant> findParticipantByUtilisateur(Utilisateur utilisateur);

    Participant findParticipantByStatus(String status);


  //  List<Participant> findParticipantByEntretien(Long id);
}
