package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.NoteResponse;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.NoteRepository;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.NoteService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final CritereRepository critereRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PostulantRepository postulantRepository;


    @Override
    public Note saveNote(Note note, Long critereId, Long postulantId, String Jury) {
        //Recuperer le Postulant par son id
        Postulant postulant = postulantRepository.findPostulantById(postulantId);
        //Recuperer le critere par son id
        Critere critere = critereRepository.findCritereById(critereId);
        if (critere.isElimination() == true && note.getPoint()<critere.getBarem()/2f) {
            postulant.setDecisionFinal(DesisionFinal.Refuser);
        }
        postulant.setNoteFinal(postulant.getNoteFinal()+note.getPoint());
        Utilisateur utilisateur = utilisateurRepository.findByUsername(Jury);
        note.setCritere(critere);
        note.setUtilisateur(utilisateur);
        note.setPostulant(postulant);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note, Long id) {
        Note note1 = noteRepository.findNoteById(id);
        note1.setPoint(note.getPoint());
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    @Override
    public List<Note> readNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note readNoteByid(Long id) {
        return noteRepository.findNoteById(id);
    }



    @Override
    public NoteResponse GetNoteByCritere(Long IdCritere,Long idJury,Long idPostulant) {
        Note note = noteRepository.findNOteByCritereByJuryByPostulant(IdCritere,idJury,idPostulant);
        NoteResponse noteResponse = new NoteResponse();
                if(note!=null){
                    noteResponse.setNoted(true);
                    noteResponse.setContenu(note);
                }else {
                    noteResponse.setNoted(false);
                    noteResponse.setContenu(note);

                }

        return noteResponse;
    }

    @Override
    public List<Note> GetNotePostulant(Long idPostulant) {
        List<Note> note = noteRepository.findNoteByPostulant(idPostulant);
    return note;

    }
}
