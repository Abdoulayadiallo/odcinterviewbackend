package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Payload.PostulantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PostulantService {
    Postulant savePostulant(Postulant postulant, Long idEntretien);

    Postulant updatePostulant(Postulant postulant, Long id);
    void deletePostulant(Postulant postulant);
    PostulantResponse readPostulants(int pageNo, int pageSize,String sortBy, String sortDir,String genre,String nomOrprenom);
    Postulant readPostulantByid(Long id);

    void ImportPostulant(MultipartFile multipartFile,Long idEntretien);

    ByteArrayInputStream ExportPostulant();

    Postulant validerPostulant(Long idPostulant);

    Postulant refuserPostulant(Long idPostulant);
    boolean existsPostulantByEmail(String email);







}
