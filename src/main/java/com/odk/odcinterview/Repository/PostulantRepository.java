package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface PostulantRepository extends JpaRepository<Postulant,Long> {
    Postulant findPostulantById(Long id);
    boolean existsPostulantByEmail(String email);

    Page<Postulant> findByGenreContaining(String genre, Pageable pageable);
    Page<Postulant> findByNomOrPrenomContaining(String nom,String prenom, Pageable pageable);

    Postulant findPostulantByParticipant(Participant participant);

    //Custom query
    @Query(value = "select * from  postulant WHERE postulant.email like %:keyword% OR postulant.decision_final like %:keyword% or postulant.genre like %:keyword% or postulant.nom like %:keyword% or postulant.prenom like %:keyword% or postulant.numero like %:keyword% or postulant.numeromtcl like %:keyword% or postulant.note_final like %:keyword% or postulant.rang like %:keyword% or postulant.date_creation like %:keyword%", nativeQuery = true)
    Page<Postulant> findByKeyword(@RequestParam(value = "keyword", required = false) String keyword, Pageable pageable);
    @Query(value = "select DISTINCT postulant.* from postulant,participant,entretien WHERE postulant.participant_id=participant.id AND participant.entretien_id=:x", nativeQuery = true)
    Page<Postulant> findAllPostulantByEntretien(@Param("x") Long id,Pageable pageable);

    @Query(value = "select distinct postulant.* from  postulant,participant,entretien WHERE  (postulant.participant_id=participant.id AND participant.entretien_id=:x) AND (postulant.email like %:keyword% OR postulant.decision_final like %:keyword% or postulant.genre like %:keyword% or postulant.nom like %:keyword% or postulant.prenom like %:keyword% or postulant.numero like %:keyword% or postulant.numeromtcl like %:keyword% or postulant.note_final like %:keyword% or postulant.rang like %:keyword% or postulant.date_creation like %:keyword%)", nativeQuery = true)
    Page<Postulant> findPostulantEntretien0rByKeyword(@Param("x") Long id,@RequestParam(value = "keyword", required = false) String keyword, Pageable pageable);
}
