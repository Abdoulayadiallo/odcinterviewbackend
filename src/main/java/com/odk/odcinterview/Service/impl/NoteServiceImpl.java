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
        if (critere.isElimination() == true) {
            postulant.setDecisionFinal(DesisionFinal.Refuser);
        }
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
        Critere critere = critereRepository.findCritereById(IdCritere);
        Utilisateur jury = utilisateurRepository.findUtilisteurById(idJury);
        Postulant postulant= postulantRepository.findPostulantById(idPostulant);
        List<Note> note = noteRepository.findNoteByCritere(critere);
        NoteResponse noteResponse = new NoteResponse();
        for (Note note1:note){
                Postulant postulant1= note1.getPostulant();
                Utilisateur utilisateur = note1.getUtilisateur();
                if(postulant1.equals(postulant) && utilisateur.equals(jury)){
                    noteResponse.setNoted(true);
                    noteResponse.setContenu(note1);
                    noteResponse.setPostulant(postulant1.getPrenom() +" "+ postulant1.getNom());
                    noteResponse.setUtilisateur(jury.getPrenom() +" "+ jury.getNom());
                   // postulantList.add(postulant1);
                }else {
                    noteResponse.setNoted(false);
                    noteResponse.setUtilisateur(jury.getPrenom() + jury.getNom());
                    noteResponse.setPostulant(postulant.getPrenom() + postulant.getNom());
                }
            }

        return noteResponse;
    }
}
