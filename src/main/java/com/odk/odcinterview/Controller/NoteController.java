package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Autowired
    EntretienService entretienService;
    @Autowired
    QuestionService questionService;
    @Autowired
    CritereService critereService;
    @Autowired
    NoteService noteService;
    @Autowired
    PostulantService postulantService;
    @Autowired
    AccountService accountService;

    // methode permettant de recuperer une note
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionInfo(@PathVariable Long id) {
        Note note= noteService.readNoteByid(id);
        if (note == null) {
            return new ResponseEntity<>("Ce note n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }
    // methode permettant d'ajouter une note

    @PostMapping("/add/{critereId}/{postulantId}/{Jury}")
    public ResponseEntity<?> addNote(@PathVariable Long critereId,@PathVariable Long postulantId,@PathVariable String Jury,@RequestBody Note note) {
        Postulant postulant = postulantService.readPostulantByid(postulantId);
        if(postulant == null) {
            return new ResponseEntity<>("Ce postulant n existe pas.", HttpStatus.OK);
        }
        Critere critere = critereService.readCritereByid(critereId);
        if(critere == null) {
            return new ResponseEntity<>("Ce critere n existe pas.", HttpStatus.OK);
        }
        Utilisateur utilisateur = accountService.findByUsername(Jury);
        if(utilisateur == null) {
            return new ResponseEntity<>("Ce jury n existe pas.", HttpStatus.OK);
        }
        if(noteService.readNoteByid(note.getId()) != null) {
            return new ResponseEntity<>("Cette note existe deja.", HttpStatus.OK);
        }
        if(note.getPoint() > critere.getBarem() || note.getPoint()<0){
            return new ResponseEntity<>("Veuillez saisir une bonne note",HttpStatus.BAD_REQUEST);
        }
        noteService.saveNote(note,critereId,postulantId,Jury);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }
    // methode permettant de modifier une Note

    @PutMapping("/update/{idNote}")
    public ResponseEntity<?> updateNote(@PathVariable Long idNote,@RequestBody Note note) {
        if (noteService.readNoteByid(idNote) == null) {
            return new ResponseEntity<>("Cette note n existe pas.", HttpStatus.NOT_FOUND);
        }
        Note note1=noteService.updateNote(note,idNote);
        return new ResponseEntity<>(note1, HttpStatus.OK);
    }
    //methode permettant de supprimer une Note

    @DeleteMapping("/delete/{idNote}")
    public ResponseEntity<?> deleteNote(@PathVariable Long idNote) {
        Note note = noteService.readNoteByid(idNote);
        if (note == null) {
            return new ResponseEntity<>("Cette note n existe pas.", HttpStatus.NOT_FOUND);
        }
        noteService.deleteNote(note);
        return new ResponseEntity<>("Note a ete supprime avec succes", HttpStatus.OK);
    }
    //methode permettant de recuperer tous les Notes
    @GetMapping("/list")
    public ResponseEntity<?> getNoteList() {
        List<Note> notes = noteService.readNotes();
        if (notes.isEmpty()){
            return new ResponseEntity<>("Pas encore de Notes.", HttpStatus.OK);
        }
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
    @GetMapping("/critere/{idCritere}/{idJury}/{idPostulant}")
    public ResponseEntity<?> getNoteByCritere(@PathVariable Long idCritere,@PathVariable Long idJury,@PathVariable Long idPostulant) {
        if (critereService.readCritereByid(idCritere) == null){
            return new ResponseEntity<>("Critere non trouvé.", HttpStatus.OK);
        }
        if (postulantService.readPostulantByid(idPostulant) == null){
            return new ResponseEntity<>("Postulant non trouvé.", HttpStatus.OK);
        }
        if (accountService.findUserById(idJury) == null){
            return new ResponseEntity<>("Jury non trouvé.", HttpStatus.OK);
        }

        return new ResponseEntity<>(noteService.GetNoteByCritere(idCritere,idJury,idPostulant), HttpStatus.OK);
    }
    @GetMapping("/postulant/{idPostulant}")
    public ResponseEntity<?> getNoteByPostulant(@PathVariable Long idPostulant) {

        if (postulantService.readPostulantByid(idPostulant) == null){
            return new ResponseEntity<>("Postulant non trouvé.", HttpStatus.OK);
        }

        return new ResponseEntity<>(noteService.GetNotePostulant(idPostulant), HttpStatus.OK);
    }



}
