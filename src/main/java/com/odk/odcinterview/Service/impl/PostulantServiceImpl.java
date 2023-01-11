package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Estatus;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Service.ParticipantService;
import com.odk.odcinterview.Service.PostulantService;
import com.odk.odcinterview.util.ExcelHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class PostulantServiceImpl implements PostulantService {
    private final PostulantRepository postulantRepository;

    public PostulantServiceImpl(PostulantRepository postulantRepository) {
        this.postulantRepository = postulantRepository;
    }

    @Override
    public Postulant savePostulant(Postulant postulant) {
        return postulantRepository.save(postulant);
    }

    @Override
    public Postulant updatePostulant(Postulant postulant, Long id) {
        return null;
    }

    @Override
    public void deletePostulant(Postulant postulant) {
        postulantRepository.delete(postulant);
    }

    @Override
    public List<Postulant> readPostulants() {
        return null;
    }

    @Override
    public Postulant readPostulantByid(Long id) {
        return null;
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
}
