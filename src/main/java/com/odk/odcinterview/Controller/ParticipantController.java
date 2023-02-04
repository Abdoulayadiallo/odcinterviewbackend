package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Repository.ParticipantRepository;
import com.odk.odcinterview.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    EntretienService entretienService;
    @Autowired
    CritereService critereService;
    @Autowired
    QuestionService questionService;
    @Autowired
    NoteService noteServicee;
    @Autowired
    PostulantService postulantService;

    @Autowired
    AccountService accountService;
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getParticipantInfo(@PathVariable Long id) {
        Participant participant= participantService.readParticipantByid(id);
        if (participant == null) {
            return new ResponseEntity<>("Ce postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<?> getParticipantList() {
        List<Participant> Participants = participantService.readParticipants();
        if (Participants.isEmpty()){
            return new ResponseEntity<>("Pas encore de Participants.", HttpStatus.OK);
        }
        return new ResponseEntity<>(Participants, HttpStatus.OK);
    }
    @GetMapping("/jury/{id}")
    public ResponseEntity<?> getParticipantByJury(@PathVariable Long id) {
        if(accountService.findUserById(id)==null){
            return new ResponseEntity<>("utilisateur non trouve",HttpStatus.BAD_REQUEST);
        }
        Participant participant = participantService.getParticipantByJury(id);
        return new ResponseEntity<>(participant,HttpStatus.OK);
    }
}
