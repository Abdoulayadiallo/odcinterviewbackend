package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Payload.NombreResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PostulantService {
    Postulant savePostulant(Postulant postulant, Long idEntretien);

    Postulant updatePostulant(Postulant postulant, Long id);
    void deletePostulant(Postulant postulant);
    PostulantResponse readPostulants(int pageNo, int pageSize,String sortBy, String sortDir,String genre,String nomOrprenom);
    PostulantResponse readPostulantsBYENTRETIEN(Long idEntretien,int pageNo, int pageSize,String sortBy, String sortDir,String genre,String keyword);
    Postulant readPostulantByid(Long id);

    void ImportPostulant(MultipartFile multipartFile,Long idEntretien);

    ByteArrayInputStream ExportPostulantByEntretien(Long idEntretien);
    ByteArrayInputStream ExportPostulant();

    Postulant validerPostulant(Long idPostulant,String commentaireFinal);

    Postulant refuserPostulant(Long idPostulant,String commentaireFinal);
    Postulant enattentePostulant(Long idPostulant,String commentaireFinal);
    boolean existsPostulantByEmail(String email);

  //  PostulantResponse getPostulantByEntretien(Long idEntretien,int pageNo, int pageSize,String sortBy, String sortDir);

    NombreResponse getNombre(String genre, Long idEntretien);
    NombreResponse getNombreAllPostulant(String genre);

    PostulantResponse findPostulantsByUtilisateurAndEntretien(Long idEntretien, Long idUtilisateur,int pageNo, int pageSize, String sortBy, String sortDir, String keyword);

    PostulantResponse findPostulantsByUtilisateur(Long idUtilisateur,int pageNo, int pageSize, String sortBy, String sortDir, String keyword);


    List<Postulant> trierPostulantsParNote(Entretien entretien);
    boolean verifierPosulantEvaluer(Long idPostulant);



}
