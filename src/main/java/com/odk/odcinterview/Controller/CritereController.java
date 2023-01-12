package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/critere")
public class CritereController {
    @Autowired
    CritereService critereService;
    @Autowired
    QuestionService questionService;
    @Autowired
    NoteService noteServicee;
    @Autowired
    PostulantService postulantService;

    //methode permettant de recuperer un Critere
    @GetMapping("/{id}")
    public ResponseEntity<?> getCritereInfo(@PathVariable Long id) {
        Critere critere= critereService.readCritereByid(id);
        if (critere == null) {
            return new ResponseEntity<>("Critere n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(critere, HttpStatus.OK);
    }
    //methode permettant d'ajouter un critere avec sa question

    @PostMapping("/add/{idQuestion}")
    public ResponseEntity<?> addCritere(@PathVariable Long idQuestion,@RequestBody Critere critere) {
        Question question = questionService.readQuestionByid(idQuestion);
        if(question == null){
            return new ResponseEntity<>("Cette question n existe pas",HttpStatus.NOT_FOUND);
        }
        if (critereService.readCritereByid(critere.getId()) != null) {
            return new ResponseEntity<>("ce critere existe deja.", HttpStatus.NOT_FOUND);
        }
        critereService.saveCritere(critere,idQuestion);
        return new ResponseEntity<>(critere, HttpStatus.CREATED);
    }
    //methode permettant de modifier un critere

    @PutMapping("/update/{idCritere}")
    public ResponseEntity<?> updateCritere(@PathVariable Long idCritere,@RequestBody Critere critere) {
        if (critereService.readCritereByid(idCritere) == null) {
            return new ResponseEntity<>("cette critere n existe pas.", HttpStatus.NOT_FOUND);
        }
        Critere critere1 = critereService.updateCritere(critere,idCritere);
        return new ResponseEntity<>(critere1, HttpStatus.OK);
    }
    //methode permettant de supprimer un critere

    @DeleteMapping("/delete/{idCritere}")
    public ResponseEntity<?> deleteCritere(@PathVariable Long idCritere) {
        Critere critere = critereService.readCritereByid(idCritere);
        if (critere == null) {
            return new ResponseEntity<>("ce critere n existe pas.", HttpStatus.NOT_FOUND);
        }
        critereService.deleteCritere(critere);
        return new ResponseEntity<>("Critere a ete supprime avec succes", HttpStatus.OK);
    }
    //methode permettant de recuperer tous les criteres
    @GetMapping("/list")
    public ResponseEntity<?> getCritereList() {
        List<Critere> criteres = critereService.readCriteres();
        if (criteres.isEmpty()){
            return new ResponseEntity<>("Pas encore de Criteres.", HttpStatus.OK);
        }
        return new ResponseEntity<>(criteres, HttpStatus.OK);
    }

}
