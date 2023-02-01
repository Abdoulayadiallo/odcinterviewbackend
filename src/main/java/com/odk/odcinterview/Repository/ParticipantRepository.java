package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    Participant findParticipantByEmail(String Email);

    Participant findParticipantByStatus(String status);


    List<Participant> findParticipantByEntretien(Long id);
}
