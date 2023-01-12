package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Question;
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

    // methode permettant de recuperer une note
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionInfo(@PathVariable Long id) {
        Note note= noteService.readNoteByid(id);
        if (note == null) {
            return new ResponseEntity<>("Note n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }
    // methode permettant d'ajouter une note

    @PostMapping("/add/{idPostulant}")
    public ResponseEntity<?> addQuestion(@PathVariable Long idPostulant,@RequestBody Note note) {
        Postulant postulant = postulantService.readPostulantByid(idPostulant);
        if(postulant == null) {
            return new ResponseEntity<>("Ce postulant existe deja.", HttpStatus.OK);
        }

        if(noteService.readNoteByid(note.getId()) != null) {
            return new ResponseEntity<>("Cette note existe deja.", HttpStatus.OK);
        }
        noteService.saveNote(note,idPostulant);
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

}
