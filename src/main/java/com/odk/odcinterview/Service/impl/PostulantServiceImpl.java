package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Estatus;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Service.ParticipantService;
import com.odk.odcinterview.Service.PostulantService;
import com.odk.odcinterview.util.ExcelHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostulantServiceImpl implements PostulantService {
    private final PostulantRepository postulantRepository;

    @Override
    public Postulant savePostulant(Postulant postulant) {
        return postulantRepository.save(postulant);
    }

    @Override
    public Postulant updatePostulant(Postulant postulant, Long id) {
        Postulant postulant1 = postulantRepository.findPostulantById(id);
        postulant1.setNumero(postulant.getNumero());
        postulant1.setNom(postulant.getNom());
        postulant1.setPrenom(postulant.getPrenom());
        postulant1.setEmail(postulant.getEmail());
        postulant1.setGenre(postulant.getGenre());
        return postulantRepository.save(postulant1);
    }

    @Override
    public void deletePostulant(Postulant postulant) {
        postulantRepository.delete(postulant);
    }

    @Override
    public List<Postulant> readPostulants() {
        return postulantRepository.findAll();
    }

    @Override
    public Postulant readPostulantByid(Long id) {
        return postulantRepository.findPostulantById(id);
    }

    @Override
    public void ImportPostulant(MultipartFile multipartFile) {
        try {
            List<Postulant>  postulants = ExcelHelper.excelToPostulants(multipartFile.getInputStream());
            postulantRepository.saveAll(postulants);
        }catch (IOException e){
            throw new RuntimeException("Echec du stockage de données excels",e);
        }
    }

    @Override
    public ByteArrayInputStream ExportPostulant() {
        List<Postulant> postulants = postulantRepository.findAll();
        ByteArrayInputStream inputStream = ExcelHelper.postulantsToExcel(postulants);
        return inputStream;
    }
}
