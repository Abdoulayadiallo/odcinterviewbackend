package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.NoteResponse;
import com.odk.odcinterview.Repository.*;
import com.odk.odcinterview.Service.NoteService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final CritereRepository critereRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PostulantRepository postulantRepository;
    private final EntretienRepository entretienRepository;


    @Override
    public Note saveNote(Note note, Long critereId, Long postulantId, String Jury) {
        //Recuperer le Postulant par son id
        Postulant postulant = postulantRepository.findPostulantById(postulantId);
        //Recuperer le critere par son id
        Critere critere = critereRepository.findCritereById(critereId);
        if (critere.isElimination() == true && note.getPoint()<critere.getBarem()/2f) {
            postulant.setDecisionFinal(DesisionFinal.Refuser);
        }

        postulant.setNoteFinal(postulant.getNoteFinal()+note.getPoint()*critere.getBarem());
        Utilisateur utilisateur = utilisateurRepository.findByUsername(Jury);
        note.setCritere(critere);
        note.setUtilisateur(utilisateur);
        note.setPostulant(postulant);

        List<Postulant> postulants = entretienRepository.findEntretienByPostulants(postulant).getPostulants();
        for (Postulant postulant1 : postulants) {
            double total = 0;
            double baremTotal = 0;
            List<Note> notes = postulant1.getNotes();
            for (Note note1 : notes) {
                total += note1.getPoint() * note1.getCritere().getBarem();
                System.out.println(total + "Total");
                baremTotal += note1.getCritere().getBarem();
                System.out.println(baremTotal + "baremtotal");
            }
            if(baremTotal!=0){
                double noteFinale = total / baremTotal;
                postulant1.setNoteFinal(noteFinale);
            }
        }
        Note note1= noteRepository.save(note);

        postulants.sort(Comparator.comparingDouble(Postulant::getNoteFinal).reversed());

        int rang = 1;
        for (Postulant postulant1 : postulants) {
            postulant1.setRang(rang++);
        }
        postulantRepository.saveAll(postulants);
        return note1;
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
