package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.EntretienResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Service.*;
import com.odk.odcinterview.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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

    //methode permettant de recuperer un entretien


    @GetMapping("/{id}")
    public ResponseEntity<?> getEntretienInfo(@PathVariable Long id) {
        Entretien entretien= entretienService.readEntretienByid(id);
        if (entretien == null) {
            return new ResponseEntity<>("Cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entretien, HttpStatus.OK);
    }
    //methode permettant d'ajouter un entretien avec son critere


    @PostMapping("/add/")
    public ResponseEntity<?> addEntretien(@RequestBody Entretien entretien) {
//        Critere critere = critereService.readCritereByid(idCritere);
//        if(critere == null){
//            return new ResponseEntity<>("Ce critere n existe pas",HttpStatus.NOT_FOUND);
//        }
        if (entretienService.readEntretienByid(entretien.getId()) != null) {
            return new ResponseEntity<>("cet entretien existe deja.", HttpStatus.NOT_FOUND);
        }
        entretienService.saveEntretien(entretien);
        return new ResponseEntity<>(entretien, HttpStatus.CREATED);
    }
    //methode permettant de modifier un entretien


    @PutMapping("/update/{idEntretien}")
    public ResponseEntity<?> updateEntretien(@PathVariable Long idEntretien,@RequestBody Entretien entretien) {
        if (entretienService.readEntretienByid(idEntretien) == null) {
            return new ResponseEntity<>("cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        Entretien entretien1= entretienService.updateEntretien(entretien,idEntretien);
        return new ResponseEntity<>(entretien1, HttpStatus.OK);
    }
    //methode permettant de supprimer un entretien


    @DeleteMapping("/delete/{idEntretien}")
    public ResponseEntity<?> deleteEntretien(@PathVariable Long idEntretien) {
        Entretien entretien = entretienService.readEntretienByid(idEntretien);
        if (entretien == null) {
            return new ResponseEntity<>("cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        entretienService.deleteEntretien(entretien);
        return new ResponseEntity<>("entretien a ete supprime avec succes", HttpStatus.OK);
    }
    //methode permettant de recuperer tous les entretiens
    @GetMapping("/list")
    public ResponseEntity<?> getEntretienList(
            @RequestParam(value = "pageNo" ,defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize" ,defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        EntretienResponse entretiens = entretienService.readEntretiens(pageNo,pageSize,sortBy,sortDir,username,keyword);
        if (entretiens.getTotalElements()==0){
            return new ResponseEntity<>("Pas encore d'entretiens.", HttpStatus.OK);
        }
        return new ResponseEntity<>(entretiens, HttpStatus.OK);
    }

    @PostMapping("/photo/upload/{id}")
    public ResponseEntity<String> fileUpload(@RequestParam("image") MultipartFile multipartFile,@PathVariable Long id) {
        Entretien entretien = entretienService.readEntretienByid(id);
        if (entretien == null) {
            return new ResponseEntity<>("cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        try {
            entretienService.saveEntretienImage(multipartFile, entretien.getEntretienNom());
            return new ResponseEntity<>("Entretien image enregistrer!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Entretien image non enregistrer", HttpStatus.BAD_REQUEST);
        }
    }


}
