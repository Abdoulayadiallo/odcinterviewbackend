package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostulantService {
    Postulant savePostulant(Postulant postulant);
    Postulant updatePostulant(Postulant postulant,Long id);
    void deletePostulant(Postulant postulant);
    List<Postulant> readPostulants();
    Postulant readPostulantByid(Long id);

    void ImportPostulant(MultipartFile multipartFile);


}
