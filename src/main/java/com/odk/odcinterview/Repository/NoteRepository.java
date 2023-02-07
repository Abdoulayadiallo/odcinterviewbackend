package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    Note findNoteById(Long id);
    List<Note> findNoteByCritere(Critere critere);
//    List<Note> findNoteByPostulant(Postulant postulant);
//
//    List<Note> findNoteByUtilsateur(Utilisateur utilisateur);
    @Query(value = "SELECT * FROM note WHERE note.utilisateur_id=:idJury AND note.postulant_id=:idPostulant AND note.critere_id=:idCritere",nativeQuery = true)
    Note findNOteByCritereByJuryByPostulant(@Param("idCritere") Long idCritere,@Param("idJury") Long idjury,@Param("idPostulant") Long idPodtulant);
    @Query(value = "SELECT note.* FROM note,critere WHERE note.postulant_id =:idPostulant AND note.critere_id = critere.id",nativeQuery = true)
    List<Note> CritereNoteByPostulant(@Param("idPostulant") Long id);
}
