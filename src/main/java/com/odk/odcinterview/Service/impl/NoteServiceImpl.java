package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
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
    public Note saveNote(Note note, Long critereId,Long postulantId,String Jury) {
        //Recuperer le Postulant par son id
        Postulant postulant = postulantRepository.findPostulantById(postulantId);
        //Recuperer le critere par son id
        Critere critere = critereRepository.findCritereById(critereId);
        if(critere.isElimination()==true){
            postulant.setDecisionFinal(DesisionFinal.Refuser);
        }
        //Recuperer le participant par son postulant
        Participant p=postulant.getParticipant();
        // Creer d'une liste pour stocker le postulant et le jury
        List<Participant> participants = new ArrayList<>();
        //Ajout du postulant à la liste
        participants.add(p);
        //Recuperer le jury par son username
        Utilisateur utilisateur = utilisateurRepository.findByUsername(Jury);
        //Recuperer le participant par son jury
        Participant j = utilisateur.getParticipant();
        //Ajout du jury à la liste
        participants.add(j);
        //Ajout des participants à note
        note.setParticipants(participants);

        //Ajout du critère à note
        note.setCritere(critere);
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
    public Note GetNoteByCritere(Long IdCritere) {
        Critere critere = critereRepository.findCritereById(IdCritere);
        Note note = noteRepository.findNoteByCritere(critere);
        return note;
    }
}
