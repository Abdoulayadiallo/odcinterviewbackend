package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Service.*;
import com.odk.odcinterview.util.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/postulant")
public class PostulantController {
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


    @PostMapping("/upload/{idEntretien}")
    public ResponseEntity<?> uplodPostlulant(@PathVariable Long idEntretien,@RequestParam("file") MultipartFile file) {

        Entretien entretien= entretienService.readEntretienByid(idEntretien);
        if (entretien == null) {
            return new ResponseEntity<>("Cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        if (ExcelHelper.hasExcelFormat(file)) {
            postulantService.ImportPostulant(file,idEntretien);

            return new ResponseEntity<>("Fichier importer avec succès.", HttpStatus.OK);

        }
        return null;
    }

    // Export de la liste des postulants
    @GetMapping("/download")
    public ResponseEntity<Resource> getFile() {
        Date date = new Date();
        String filename = "postulant" + date.toString() + ".xlsx";
        InputStreamResource file = new InputStreamResource(postulantService.ExportPostulant());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
    //methode permettant de recuperer un postulant
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionInfo(@PathVariable Long id) {
        Postulant postulant= postulantService.readPostulantByid(id);
        if (postulant == null) {
            return new ResponseEntity<>("Ce postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postulant, HttpStatus.OK);
    }
    //methode permettant d'ajouter un postulant

    @PostMapping("/add")
    public ResponseEntity<?> addPostulant(@RequestBody Postulant postulant) {
        if (postulantService.readPostulantByid(postulant.getId()) != null) {
            return new ResponseEntity<>("Ce postulant existe deja.", HttpStatus.NOT_FOUND);
        }
        postulantService.savePostulant(postulant);
        return new ResponseEntity<>(postulant, HttpStatus.CREATED);
    }
    //methode permettant de modifier un Postulant

    @PutMapping("/update/{idPostulant}")
    public ResponseEntity<?> updatePostulant(@PathVariable Long idPostulant,@RequestBody Postulant postulant) {
        if (postulantService.readPostulantByid(idPostulant) == null) {
            return new ResponseEntity<>("Ce postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        Postulant postulant1 = postulantService.updatePostulant(postulant,idPostulant);
        return new ResponseEntity<>(postulant1, HttpStatus.OK);
    }
    //methode permettant de supprimer une Postulant

    @DeleteMapping("/delete/{idPostulant}")
    public ResponseEntity<?> deletePostulant(@PathVariable Long idPostulant) {
        Postulant postulant = postulantService.readPostulantByid(idPostulant);
        if (postulant == null) {
            return new ResponseEntity<>("Ce Postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        postulantService.deletePostulant(postulant);
        return new ResponseEntity<>("Postulant a ete supprime avec succes", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUsersList(
            @RequestParam(value = "pageNo" ,defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize" ,defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostulantResponse postulants = postulantService.readPostulants(pageNo,pageSize,sortBy,sortDir);
        if (postulants.getContenu().isEmpty()) {
            return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
        }
        return new ResponseEntity<>(postulants, HttpStatus.OK);
    }


}
