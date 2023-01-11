package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Service.*;
import com.odk.odcinterview.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/entretien")
public class EntretienController {
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


    @PostMapping("/postulant/upload")
    public ResponseEntity<?> uplodPostlulant(@RequestParam("file") MultipartFile file) {

        if (ExcelHelper.hasExcelFormat(file)) {
                postulantService.ImportPostulant(file);

                return new ResponseEntity<>("Fichier importer avec succès.", HttpStatus.OK);

        }
        return null;
    }

        @GetMapping("/postulant")
        public ResponseEntity<?> getUsersList() {
            List<Postulant> postulants = postulantService.readPostulants();
            if (postulants.isEmpty()) {
                return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
            }
            return new ResponseEntity<>(postulants, HttpStatus.OK);
        }



}
