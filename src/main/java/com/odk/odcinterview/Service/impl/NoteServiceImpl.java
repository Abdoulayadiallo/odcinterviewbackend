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
        //Recuperer le participant par son postulant
      //  Participant p = postulant.getParticipant();
        // Creer d'une liste pour stocker le postulant et le jury
        //List<Participant> participants = new ArrayList<>();
        //Ajout du postulant à la liste
        //participants.add(p);
        //Recuperer le jury par son username
        Utilisateur utilisateur = utilisateurRepository.findByUsername(Jury);
        //Recuperer le participant par son jury
        //Participant j = utilisateur.getParticipant();
        //Ajout du jury à la liste
        //participants.add(j);
        //Ajout des participants à note
       // note.setParticipants(participants);
        Participant participant = new Participant();
        participant.setPostulant(postulant);
        participant.setUtilisateur(utilisateur);
        //Ajout du critère à note
        note.setCritere(critere);
        note.setParticipant(participant);
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
    public NoteResponse GetNoteByCritere(Long IdCritere, Long idJury, Long idPostulant) {
        return null;
    }


//    @Override
//    public NoteResponse GetNoteByCritere(Long IdCritere,Long idJury,Long idPostulant) {
//        Critere critere = critereRepository.findCritereById(IdCritere);
//        Utilisateur jury = utilisateurRepository.findUtilisteurById(idJury);
//        Postulant postulant= postulantRepository.findPostulantById(idPostulant);
//        List<Note> note = noteRepository.findNoteByCritere(critere);
//        NoteResponse noteResponse = new NoteResponse();
//        for (Note note1:note){
//            Collection<Participant> utilisateurList = note1.getParticipants();
//            for (Participant participant:utilisateurList){
//                Postulant postulant1 = postulantRepository.findPostulantByParticipant(participant);
//                Utilisateur utilisateur = utilisateurRepository.findUtilisateurByParticipant(participant);
//                //List<Utilisateur> utilisateurList1 = new ArrayList<>();
//                //List<Postulant> postulantList = new ArrayList<>();
//                if(postulant1 != null && postulant1.equals(postulant) && utilisateur!=null && utilisateur.equals(jury)){
//                    noteResponse.setNoted(true);
//                    noteResponse.setContenu(note1);
//                    noteResponse.setPostulant(postulant1.getPrenom() + postulant1.getNom());
//                    noteResponse.setUtilisateur(utilisateur.getPrenom() + utilisateur.getNom());
//                   // postulantList.add(postulant1);
//                }else {
//                    noteResponse.setNoted(false);
//                    noteResponse.setUtilisateur(jury.getPrenom() + jury.getNom());
//                    noteResponse.setPostulant(postulant.getPrenom() + postulant.getNom());
//                }
//            }
//        }
//
//        return noteResponse;
//    }
    ////////////////////////
}
