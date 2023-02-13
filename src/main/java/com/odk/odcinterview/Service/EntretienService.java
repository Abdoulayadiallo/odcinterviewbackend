package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Payload.EntretienResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EntretienService {
    Entretien saveEntretien(Entretien entretien);
    Entretien updateEntretien(Entretien entretien,Long id);
    void deleteEntretien(Entretien entretien);
    EntretienResponse readEntretiens(int pageNo, int pageSize, String sortBy, String sortDir,String username);
    Entretien readEntretienByid(Long id);

    String saveEntretienImage(MultipartFile multipartFile, String entretienImageNom);

}
