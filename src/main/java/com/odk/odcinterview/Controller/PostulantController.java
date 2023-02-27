package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Payload.NombreResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/download/{idEntretien}")
    public ResponseEntity<Resource> getFile(@PathVariable Long idEntretien) {
        Entretien entretien = entretienService.readEntretienByid(idEntretien);
        Date date = new Date();
        String filename = "postulant_de_"+entretien.getEntretienNom()+ date.toString() + ".xlsx";
        InputStreamResource file = new InputStreamResource(postulantService.ExportPostulantByEntretien(idEntretien));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> getFilePostulantAll() {
        Date date = new Date();
        String filename = "tous_postulants.xlsx";
        InputStreamResource file = new InputStreamResource(postulantService.ExportPostulant());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
    //methode permettant de recuperer un postulant
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostulantInfo(@PathVariable Long id) {
        Postulant postulant= postulantService.readPostulantByid(id);
        if (postulant == null) {
            return new ResponseEntity<>("Ce postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postulant, HttpStatus.OK);
    }
    //methode permettant d'ajouter un postulant
    @PostMapping("/add/{idEntretien}")
    public ResponseEntity<?> addPostulant(@PathVariable Long idEntretien, @RequestBody Postulant postulant) {
        Entretien entretien = entretienService.readEntretienByid(idEntretien);
        if (postulantService.existsPostulantByEmail(postulant.getEmail())) {
            return new ResponseEntity<>("Ce postulant existe deja.", HttpStatus.EXPECTATION_FAILED);
        }
        if (entretien == null) {
            return new ResponseEntity<>("Cet entretien n existe pas.", HttpStatus.NOT_FOUND);
        }
        postulantService.savePostulant(postulant,idEntretien);
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

    @PostMapping("/accepter/{idPostulant}")
    public ResponseEntity<?> accepterPostulant(@PathVariable Long idPostulant,@RequestBody String commentaireFinal) {
        Postulant postulant = postulantService.readPostulantByid(idPostulant);
        if (postulant == null) {
            return new ResponseEntity<>("Ce Postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        postulantService.validerPostulant(idPostulant,commentaireFinal);
        return new ResponseEntity<>("Postulant a ete accepter", HttpStatus.OK);
    }
    @PostMapping("/refuser/{idPostulant}")
    public ResponseEntity<?> refuserPostulant(@PathVariable Long idPostulant,@RequestBody String commentaireFinal) {
        Postulant postulant = postulantService.readPostulantByid(idPostulant);
        if (postulant == null) {
            return new ResponseEntity<>("Ce Postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        postulantService.refuserPostulant(idPostulant,commentaireFinal);
        return new ResponseEntity<>("Postulant a ete refuser", HttpStatus.OK);
    }
    @PostMapping("/enattente/{idPostulant}")
    public ResponseEntity<?> enAttentePostulant(@PathVariable Long idPostulant,@RequestBody String commentaireFinal) {
        Postulant postulant = postulantService.readPostulantByid(idPostulant);
        if (postulant == null) {
            return new ResponseEntity<>("Ce Postulant n existe pas.", HttpStatus.NOT_FOUND);
        }
        postulantService.enattentePostulant(idPostulant,commentaireFinal);
        return new ResponseEntity<>("Postulant a ete mis à la liste d'attente", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUsersList(
            @RequestParam(value = "pageNo" ,defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize" ,defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PostulantResponse postulants = postulantService.readPostulants(pageNo,pageSize,sortBy,sortDir,genre,keyword);
        //if (postulants.getContenu().isEmpty()) {
        //    return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
        //}
        return new ResponseEntity<>(postulants, HttpStatus.OK);
    }
    @GetMapping("/list/PostulantEntretien/{idEntretien}")
    public ResponseEntity<?> getPostulantListByEntretien(
            @PathVariable Long idEntretien,
            @RequestParam(value = "pageNo" ,defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize" ,defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PostulantResponse postulants = postulantService.readPostulantsBYENTRETIEN(idEntretien,pageNo,pageSize,sortBy,sortDir,genre,keyword);
        //if (postulants.getContenu().isEmpty()) {
        //    return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
        //}
        return new ResponseEntity<>(postulants, HttpStatus.OK);
    }

    @GetMapping("/list/PostulantUtilisateur/{idEntretien}/{idUtilisateur}")
    public ResponseEntity<?> getPostulantListByEntretienAndByUtilisateur(
            @PathVariable Long idEntretien,
            @PathVariable Long idUtilisateur,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PostulantResponse postulants = postulantService.findPostulantsByUtilisateurAndEntretien(idEntretien,idUtilisateur, pageNo, pageSize, sortBy, sortDir, keyword);
        //if (postulants.getContenu().isEmpty()) {
        //    return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
        //}
        return new ResponseEntity<>(postulants, HttpStatus.OK);
    }
    @GetMapping("/list/utilisateur/{idUtilisateur}")
    public ResponseEntity<?> getPostulantListByByUtilisateur(
            @PathVariable Long idUtilisateur,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PostulantResponse postulants = postulantService.findPostulantsByUtilisateur(idUtilisateur, pageNo, pageSize, sortBy, sortDir, keyword);
        //if (postulants.getContenu().isEmpty()) {
        //    return new ResponseEntity<>("Postulants non trouvés.", HttpStatus.OK);
        //}
        return new ResponseEntity<>(postulants, HttpStatus.OK);
    }

    @GetMapping("/nombreGenre/{idEntretien}/{genre}")
    public ResponseEntity<?> getNombre(@PathVariable  String genre,@PathVariable Long idEntretien){
        //if(entretienService.readEntretienByid(idEntretien)==null){
        //    return new ResponseEntity<>("entretien non trouvé",HttpStatus.BAD_REQUEST);
        //}
        NombreResponse nombreResponse = postulantService.getNombre(genre,idEntretien);
        return new ResponseEntity<>(nombreResponse,HttpStatus.OK);
    }
    @GetMapping("/nombreGenre/{genre}")
    public ResponseEntity<?> getNombreAllPostulant(@PathVariable  String genre){
        NombreResponse nombreResponse = postulantService.getNombreAllPostulant(genre);
        return new ResponseEntity<>(nombreResponse,HttpStatus.OK);
    }
    @GetMapping("/ent/{id}")
    public ResponseEntity<?> trierPostulantsParNote(@PathVariable Long id){
        Entretien entretien = entretienService.readEntretienByid(id);
        List<Postulant> postulants = postulantService.trierPostulantsParNote(entretien);
        return new ResponseEntity<>(postulants,HttpStatus.OK);
    }


}
